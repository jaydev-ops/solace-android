#!/usr/bin/env python3
"""
Solace+ Academic Mini Project Report Compiler
Converts modular Markdown sources and high-resolution assets into a publication-grade PDF
matching Dr. Babasaheb Ambedkar Technological University (DBATU) / SBMP guidelines.
"""

import os
import re
from PIL import Image as PILImage
from reportlab.lib.pagesizes import A4
from reportlab.lib import colors
from reportlab.platypus import (
    SimpleDocTemplate, Paragraph, Spacer, PageBreak, Table, TableStyle, KeepTogether
)
from reportlab.platypus import Image as RLImage
from reportlab.lib.styles import getSampleStyleSheet, ParagraphStyle
from reportlab.lib.enums import TA_CENTER, TA_JUSTIFY, TA_LEFT, TA_RIGHT
from reportlab.pdfgen import canvas

# Base directories
BASE_DIR = os.path.abspath(os.path.join(os.path.dirname(__file__), ".."))
OUTPUT_PDF = os.path.join(BASE_DIR, "Output", "Final_Project_Report.pdf")
ASSETS_DIR = os.path.join(BASE_DIR, "Assets")

# ----------------------------------------------------------------------
# 1. Custom Academic Canvas with Exact Two-Tier Numbering & Headers
# ----------------------------------------------------------------------
class AcademicCanvas(canvas.Canvas):
    def __init__(self, *args, **kwargs):
        super().__init__(*args, **kwargs)
        self._saved_page_states = []

    def showPage(self):
        self._saved_page_states.append(dict(self.__dict__))
        self._startPage()

    def save(self):
        num_pages = len(self._saved_page_states)
        for state in self._saved_page_states:
            self.__dict__.update(state)
            p = self._pageNumber
            
            # Running footer / page numbers centered at bottom (y = 36 pt)
            self.setFont("Times-Roman", 10)
            if p <= 5:
                # Pages 1 to 5: Unnumbered (Title, Certificate, Approval, Declaration, Ack)
                pass
            elif p <= 10:
                # Pages 6 to 10: Lowercase Roman numerals (i, ii, iii, iv, v)
                roman_numerals = ["i", "ii", "iii", "iv", "v"]
                idx = p - 6
                r_num = roman_numerals[idx] if idx < len(roman_numerals) else f"r{idx}"
                self.drawCentredString(A4[0] / 2.0, 36, r_num)
            else:
                # Pages 11+: Arabic numerals starting from Chapter 1 Page 1
                arabic_num = str(p - 10)
                self.drawCentredString(A4[0] / 2.0, 36, arabic_num)
                
            super().showPage()
        super().save()

# ----------------------------------------------------------------------
# 2. Typography & Styles
# ----------------------------------------------------------------------
def setup_styles():
    styles = getSampleStyleSheet()
    
    # Custom styles
    styles.add(ParagraphStyle(
        name='AcademicTitle',
        fontName='Times-Bold',
        fontSize=18,
        leading=22,
        alignment=TA_CENTER,
        spaceAfter=14
    ))
    styles.add(ParagraphStyle(
        name='AcademicSubtitle',
        fontName='Times-Roman',
        fontSize=12,
        leading=16,
        alignment=TA_CENTER,
        spaceAfter=8
    ))
    styles.add(ParagraphStyle(
        name='AcademicChapterHeader',
        fontName='Times-Bold',
        fontSize=16,
        leading=20,
        alignment=TA_CENTER,
        spaceBefore=12,
        spaceAfter=18,
        keepWithNext=True
    ))
    styles.add(ParagraphStyle(
        name='AcademicSectionHeader',
        fontName='Times-Bold',
        fontSize=13,
        leading=16,
        alignment=TA_LEFT,
        spaceBefore=14,
        spaceAfter=6,
        keepWithNext=True
    ))
    styles.add(ParagraphStyle(
        name='AcademicSubsectionHeader',
        fontName='Times-Bold',
        fontSize=11.5,
        leading=15,
        alignment=TA_LEFT,
        spaceBefore=10,
        spaceAfter=4,
        keepWithNext=True
    ))
    styles.add(ParagraphStyle(
        name='AcademicBody',
        fontName='Times-Roman',
        fontSize=11,
        leading=15.5,
        alignment=TA_JUSTIFY,
        spaceAfter=6
    ))
    styles.add(ParagraphStyle(
        name='AcademicBullet',
        fontName='Times-Roman',
        fontSize=10.5,
        leading=14.5,
        alignment=TA_JUSTIFY,
        leftIndent=15,
        firstLineIndent=-10,
        spaceAfter=4
    ))
    styles.add(ParagraphStyle(
        name='AcademicCode',
        fontName='Courier',
        fontSize=7.5,
        leading=9.5,
        alignment=TA_LEFT,
        leftIndent=10,
        spaceAfter=4
    ))
    styles.add(ParagraphStyle(
        name='AcademicCaption',
        fontName='Times-Italic',
        fontSize=9,
        leading=12,
        alignment=TA_CENTER,
        spaceBefore=4,
        spaceAfter=10
    ))
    styles.add(ParagraphStyle(
        name='AcademicTableTitle',
        fontName='Times-Bold',
        fontSize=9.5,
        leading=13,
        alignment=TA_CENTER,
        spaceBefore=8,
        spaceAfter=4,
        keepWithNext=True
    ))
    styles.add(ParagraphStyle(
        name='AcademicTableCell',
        fontName='Times-Roman',
        fontSize=7.5,
        leading=9.5,
        alignment=TA_LEFT
    ))
    styles.add(ParagraphStyle(
        name='AcademicTableCellBold',
        fontName='Times-Bold',
        fontSize=7.5,
        leading=9.5,
        alignment=TA_LEFT
    ))
    styles.add(ParagraphStyle(
        name='AcademicTableHeader',
        fontName='Times-Bold',
        fontSize=8,
        leading=10,
        alignment=TA_CENTER,
        textColor=colors.black
    ))
    return styles

# ----------------------------------------------------------------------
# 3. Flowable Helpers
# ----------------------------------------------------------------------
def create_image_flowable(image_rel_or_abs_path, caption="", max_w=440.0, max_h=280.0, styles=None):
    # Resolve path
    if os.path.isabs(image_rel_or_abs_path):
        img_path = image_rel_or_abs_path
    else:
        candidates = [
            os.path.normpath(os.path.join(BASE_DIR, "Chapters", image_rel_or_abs_path)),
            os.path.normpath(os.path.join(BASE_DIR, "Preliminary_Pages", image_rel_or_abs_path)),
            os.path.normpath(os.path.join(BASE_DIR, image_rel_or_abs_path)),
            os.path.normpath(os.path.join(BASE_DIR, re.sub(r'^\.\./', '', image_rel_or_abs_path))),
            os.path.normpath(os.path.join(ASSETS_DIR, "Diagrams", os.path.basename(image_rel_or_abs_path))),
            os.path.normpath(os.path.join(ASSETS_DIR, "Screenshots", os.path.basename(image_rel_or_abs_path))),
            os.path.normpath(os.path.join(ASSETS_DIR, "Charts", os.path.basename(image_rel_or_abs_path))),
            os.path.normpath(os.path.join(ASSETS_DIR, "Images", os.path.basename(image_rel_or_abs_path))),
        ]
        img_path = None
        for c in candidates:
            if os.path.exists(c):
                img_path = c
                break
        if not img_path:
            img_path = candidates[0]

    if not os.path.exists(img_path):
        print(f"Warning: Image not found at {img_path}")
        return []

    try:
        im = PILImage.open(img_path)
        w, h = im.size
        aspect = h / float(w)
        # Handle portrait screenshots vs horizontal diagrams
        if aspect > 1.4:
            current_max_h = 300.0
            current_max_w = 200.0
        else:
            current_max_h = max_h
            current_max_w = max_w

        target_w = min(current_max_w, float(w))
        target_h = target_w * aspect
        if target_h > current_max_h:
            target_h = current_max_h
            target_w = target_h / aspect

        flowables = [
            Spacer(1, 4),
            RLImage(img_path, width=target_w, height=target_h),
        ]
        if caption and styles:
            flowables.append(Paragraph(caption, styles['AcademicCaption']))
        flowables.append(Spacer(1, 4))
        return flowables
    except Exception as e:
        print(f"Error loading image {img_path}: {e}")
        return []

def format_md_inline(text):
    if not text:
        return ""
    # Escape standalone & that is not part of an XML entity
    text = re.sub(r'&(?!(?:amp|lt|gt|quot|apos|#\d+);)', '&amp;', text)
    # Bold **text**
    text = re.sub(r'\*\*(.*?)\*\*', r'<b>\1</b>', text)
    # Inline code `code`
    text = re.sub(r'`([^`]+)`', r"<font face='Courier'>\1</font>", text)
    return text

def parse_markdown_table(table_lines, col_widths, styles):
    clean_lines = [l.strip() for l in table_lines if l.strip()]
    if len(clean_lines) < 2:
        return None
    
    header_raw = [c.strip() for c in clean_lines[0].split('|')[1:-1]]
    data_rows = []
    
    # Header cells
    header_cells = [Paragraph(f"<b>{format_md_inline(c)}</b>", styles['AcademicTableHeader']) for c in header_raw]
    data_rows.append(header_cells)
    
    # Body rows (skip separator at index 1)
    for l in clean_lines[2:]:
        cols = [c.strip() for c in l.split('|')[1:-1]]
        row_cells = []
        for i, c in enumerate(cols):
            cell_text = format_md_inline(c)
            row_cells.append(Paragraph(cell_text, styles['AcademicTableCell']))
        data_rows.append(row_cells)
        
    t = Table(data_rows, colWidths=col_widths, repeatRows=1)
    t.setStyle(TableStyle([
        ('BACKGROUND', (0,0), (-1,0), colors.HexColor('#EEEEEE')),
        ('GRID', (0,0), (-1,-1), 0.5, colors.HexColor('#333333')),
        ('VALIGN', (0,0), (-1,-1), 'TOP'),
        ('TOPPADDING', (0,0), (-1,-1), 3),
        ('BOTTOMPADDING', (0,0), (-1,-1), 3),
        ('LEFTPADDING', (0,0), (-1,-1), 4),
        ('RIGHTPADDING', (0,0), (-1,-1), 4),
    ]))
    return t

# ----------------------------------------------------------------------
# 4. Preliminary Pages Builders
# ----------------------------------------------------------------------
def build_title_page(styles):
    story = []
    story.append(Spacer(1, 20))
    story.append(Paragraph("“Solace+ : An AI-Powered Emotional Sanctuary and Somatic Nervous System Regulation Platform”", styles['AcademicTitle']))
    story.append(Spacer(1, 14))
    story.append(Paragraph("Submitted in partial fulfilment of the requirements<br/>for the degree of", styles['AcademicSubtitle']))
    story.append(Spacer(1, 6))
    story.append(Paragraph("<b>Bachelor of Technology</b><br/>in<br/><b>CSE AIML Engineering</b>", styles['AcademicSubtitle']))
    story.append(Spacer(1, 16))
    story.append(Paragraph("by<br/><br/><b>Prapti Gandhi (Roll No. BA14)</b><br/><b>Aarya Pawar (Roll No. BA43)</b>", styles['AcademicSubtitle']))
    story.append(Spacer(1, 16))
    story.append(Paragraph("<b>Guide</b><br/><b>Asst. Prof. Swapna Ambekar</b>", styles['AcademicSubtitle']))
    story.append(Spacer(1, 14))
    
    # College Emblem
    emblem_path = os.path.join(ASSETS_DIR, "Images", "ic_solace_emblem.jpg")
    story.extend(create_image_flowable(emblem_path, max_w=100, max_h=100))
    story.append(Spacer(1, 14))
    
    story.append(Paragraph("<b>Department of CSE AIML Engineering</b>", styles['AcademicSubtitle']))
    story.append(Paragraph("<b>Shri Bhagubhai Mafatlal Polytechnic And College of Engineering</b>", styles['AcademicSubtitle']))
    story.append(Paragraph("(UG Programmes Affiliated to Dr. Babasaheb Ambedkar Technological University, Lonere)", styles['AcademicSubtitle']))
    story.append(Paragraph("<b>(Ac. Yr. 2026-27)</b>", styles['AcademicSubtitle']))
    story.append(PageBreak())
    return story

def build_certificate_page(styles):
    story = []
    story.append(Spacer(1, 30))
    story.append(Paragraph("CERTIFICATE", styles['AcademicChapterHeader']))
    story.append(Spacer(1, 20))
    
    cert_text = (
        "This is to certify that the Mini project entitled <b>“Solace+ : An AI-Powered Emotional "
        "Sanctuary and Somatic Nervous System Regulation Platform”</b> is a bonafide work of "
        "<b>Prapti Gandhi (Roll No. BA14)</b> and <b>Aarya Pawar (Roll No. BA43)</b>, submitted to the Dr. Babasaheb Ambedkar Technological "
        "University in partial fulfillment of the requirement for the award of the certificate of "
        "Third year Seminar and Mini Project in Bachelor of Technology in CSE AIML Engineering."
    )
    story.append(Paragraph(cert_text, styles['AcademicBody']))
    story.append(Spacer(1, 90))
    
    # Guide signature
    guide_sig = [
        [Paragraph("_______________________<br/><b>Asst. Prof. Swapna Ambekar</b><br/>Mini Project guide", styles['AcademicSubtitle'])]
    ]
    t_guide = Table(guide_sig, colWidths=[450])
    story.append(t_guide)
    story.append(Spacer(1, 80))
    
    # Coordinator & Principal
    coord_princ = [
        [
            Paragraph("_______________________<br/><b>Third Year Co-Ordinator</b><br/>Third Year Co-Ordinator", styles['AcademicSubtitle']),
            Paragraph("_______________________<br/><b>Principal</b><br/>Principal", styles['AcademicSubtitle'])
        ]
    ]
    t_sig = Table(coord_princ, colWidths=[225, 225])
    story.append(t_sig)
    story.append(PageBreak())
    return story

def build_approval_page(styles):
    story = []
    story.append(Spacer(1, 30))
    story.append(Paragraph("CERTIFICATE OF APPROVAL OF EXAMINERS", styles['AcademicChapterHeader']))
    story.append(Spacer(1, 20))
    
    app_text = (
        "We certify that this project report entitled <b>“Solace+ : An AI-Powered Emotional "
        "Sanctuary and Somatic Nervous System Regulation Platform”</b> is bonafide record of "
        "Mini project work done by <b>Prapti Gandhi (Roll No. BA14)</b> and <b>Aarya Pawar (Roll No. BA43)</b>.<br/><br/>"
        "This Mini project is approved for the award of the marks for term work and oral exam "
        "for Third Year Seminar and Mini Project in the subject of CSE AIML Engineering for "
        "the semester V for the academic year 2026-2027."
    )
    story.append(Paragraph(app_text, styles['AcademicBody']))
    story.append(Spacer(1, 140))
    
    examiners = [
        [
            Paragraph("<b>External Examiner</b>", styles['AcademicSubtitle']),
            Paragraph("<b>Internal Examiner</b>", styles['AcademicSubtitle'])
        ]
    ]
    t_ex = Table(examiners, colWidths=[225, 225])
    story.append(t_ex)
    story.append(PageBreak())
    return story

def build_declaration_page(styles):
    story = []
    story.append(Spacer(1, 30))
    story.append(Paragraph("DECLARATION", styles['AcademicChapterHeader']))
    story.append(Spacer(1, 20))
    
    decl_text = (
        "We declare that this written submission represents my ideas in my own words and where others' "
        "ideas or words have been included, we have adequately cited and referenced the original sources. "
        "We, also declare that we have adhered to all principles of academic honesty and integrity and have "
        "not misrepresented or fabricated or falsified any idea/data/fact/source in our submission. We understand "
        "that any violation of the above will be cause for disciplinary action by the Institute and can also "
        "evoke penal action from the sources which have thus not been properly cited or from whom proper "
        "permission has not been taken when needed."
    )
    story.append(Paragraph(decl_text, styles['AcademicBody']))
    story.append(Spacer(1, 40))
    
    cand_table = [
        [Paragraph("1. <b>Prapti Gandhi (Roll No. BA14)</b>", styles['AcademicBody']), Paragraph("_______________________<br/>Signature of Candidate", styles['AcademicSubtitle'])],
        [Paragraph("2. <b>Aarya Pawar (Roll No. BA43)</b>", styles['AcademicBody']), Paragraph("_______________________<br/>Signature of Candidate", styles['AcademicSubtitle'])],
    ]
    t_cand = Table(cand_table, colWidths=[240, 210])
    story.append(t_cand)
    story.append(Spacer(1, 40))
    
    story.append(Paragraph("<b>Date:</b> 13th November 2026", styles['AcademicBody']))
    story.append(Paragraph("<b>Place:</b> Mumbai", styles['AcademicBody']))
    story.append(PageBreak())
    return story

def build_acknowledgement_page(styles):
    story = []
    story.append(Spacer(1, 30))
    story.append(Paragraph("ACKNOWLEDGEMENTS", styles['AcademicChapterHeader']))
    story.append(Spacer(1, 20))
    
    ack_text = (
        "We would like to express our sincere gratitude to our Mini project guide <b>Asst. Prof. Swapna Ambekar</b> "
        "and faculty members for their constant guidance and support throughout the project. We also extend our thanks "
        "to our department and college administration for providing us with the required facilities to successfully "
        "complete this mini project."
    )
    story.append(Paragraph(ack_text, styles['AcademicBody']))
    story.append(Spacer(1, 40))
    
    story.append(Paragraph("<b>Candidates:</b>", styles['AcademicBody']))
    story.append(Spacer(1, 10))
    story.append(Paragraph("• <b>Prapti Gandhi</b> (Roll No. BA14 &bull; praptigandhi12@gmail.com)<br/>"
                           "• <b>Aarya Pawar</b> (Roll No. BA43 &bull; aarya18072006@gmail.com)<br/><br/>"
                           "<b>Date:</b> 13th November 2026 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <b>Place:</b> Mumbai", styles['AcademicBody']))
    story.append(PageBreak())
    return story

def build_abstract_page(styles):
    story = []
    story.append(Spacer(1, 20))
    story.append(Paragraph("ABSTRACT", styles['AcademicChapterHeader']))
    story.append(Spacer(1, 14))
    
    p1 = (
        "The accelerating prevalence of mental fatigue, chronic anxiety, and autonomic nervous system "
        "dysregulation presents an unprecedented global health challenge. While mobile wellness platforms "
        "have proliferated, the vast majority deploy extractive gamification mechanisms—such as mandatory "
        "streaks, numerical scores, and guilt-inducing push notifications—which inadvertently amplify performance "
        "anxiety and cause precipitous long-term attrition. Furthermore, conventional conversational AI agents often "
        "introduce severe clinical hazards by attempting stochastic therapeutic diagnosis without deterministic "
        "safety boundaries, while relying on invasive cloud architectures that compromise intimate personal disclosures."
    )
    p2 = (
        "To resolve these structural limitations, this project presents <b>Solace+</b>, an emotionally intelligent, "
        "native Android mobile sanctuary engineered with <b>Jetpack Compose</b>, <b>Kotlin 2.2</b>, and a "
        "<b>zero-knowledge local-first data architecture</b> powered by <b>Room SQLite Database (v3)</b> and "
        "<b>Android 16 / SDK 36</b>. Solace+ is founded upon polyvagal neurobiology and non-judgmental holding "
        "space rather than gamified compliance. Its primary interface innovation is a <b>Mood-Responsive Atmospheric "
        "Canvas</b> that dynamically morphs surface luminescence, container tints, and motion kinetics within "
        "sub-16 millisecond composition frames across a calibrated seven-mood taxonomy (<i>Joyful, Calm, Steady, "
        "Tender, Restless, Stormy, Overwhelmed</i>)."
    )
    p3 = (
        "For conversational companionship, Solace+ orchestrates a dual-intelligence pipeline: it leverages Google Gemini "
        "models (<code>gemini-1.5-flash</code>, <code>gemini-2.0-flash</code>) across six tailored emotional regulation "
        "modes (<i>Listen, Understand, Calm, Reflect, Refocus, Gratitude</i>), backed by an on-device <b>Adaptive Local "
        "Sanctuary Dialogue Engine</b> that delivers warm, non-repetitive therapeutic reflections when operating offline. "
        "Crucially, the platform incorporates a deterministic <b>Four-Tier Mental Health Risk Decision & Escalation Subsystem</b> "
        "(<code>RiskDecisionEngine</code>, <code>EscalationManager</code>, <code>ContextAnalyzer</code>) that systematically "
        "intercepts emotional distress, passive ideation, and acute crises before prompts reach generative models. "
        "When emergency thresholds are met, the interface securely locks down generative streaming, displays hardcoded "
        "national helplines (<b>Tele-MANAS 14416</b>, <b>KIRAN 1800-599-0019</b>, <b>112 Emergency</b>), and enforces "
        "safety state-locking rules that prevent accidental de-escalation."
    )
    p4 = (
        "The complete software architecture was subjected to rigorous empirical testing, comprising 29 automated JUnit and "
        "Robolectric unit tests, achieving <b>100% pass rates</b> across mood mapping, weekday alignment, greeting synthesis, "
        "and multi-tier crisis interception logic. Solace+ demonstrates the viability of ethical, ambient, and somatic "
        "affective computing that safeguards human vulnerability without cloud exploitation."
    )
    story.append(Paragraph(p1, styles['AcademicBody']))
    story.append(Paragraph(p2, styles['AcademicBody']))
    story.append(Paragraph(p3, styles['AcademicBody']))
    story.append(Paragraph(p4, styles['AcademicBody']))
    story.append(PageBreak())
    return story

def build_contents_page(styles):
    story = []
    story.append(Spacer(1, 10))
    story.append(Paragraph("CONTENTS", styles['AcademicChapterHeader']))
    story.append(Spacer(1, 8))
    
    # Load markdown content table
    toc_lines = [
        "| Section | Sub | Title | Page No. |",
        "| :--- | :--- | :--- | :---: |",
        "| | | **Declaration** | |",
        "| | | **Abstract** | i |",
        "| | | **Contents** | ii |",
        "| | | **List of Figures** | iii |",
        "| | | **List of Tables** | iv |",
        "| | | **Abbreviations and Acronyms** | v |",
        "| **Chapter 1** | | **Introduction** | |",
        "| | 1.1 | Introduction and Motivation | 1 |",
        "| | 1.2 | Literature Review | 3 |",
        "| | 1.3 | Objectives | 10 |",
        "| | 1.4 | Scope of Project | 12 |",
        "| | 1.5 | Organization of Report | 13 |",
        "| **Chapter 2** | | **Fundamentals** | 14 |",
        "| **Chapter 3** | | **Project Environment & System Requirements** | 16 |",
        "| **Chapter 4** | | **Methodology** | 18 |",
        "| | 4.1 | Software Requirements | 19 |",
        "| | 4.2 | Proposed System & Workflow | 20 |",
        "| | 4.3 | System Architecture | 22 |",
        "| | 4.4 | Module Description | 23 |",
        "| | 4.5 | Implementation Details | 25 |",
        "| **Chapter 5** | | **Cost Analysis** | 27 |",
        "| **Chapter 6** | | **Testing** | 29 |",
        "| **Chapter 7** | | **Results and Discussion** | 33 |",
        "| **Chapter 8** | | **Summary** | 40 |",
        "| **Chapter 9** | | **Conclusion** | 41 |",
        "| | | **References** | 42 |",
        "| | | **Acknowledgement** | 44 |",
        "| | | **Author’s Publications** | 45 |"
    ]
    t = parse_markdown_table(toc_lines, [80, 40, 270, 61], styles)
    story.append(t)
    story.append(PageBreak())
    return story

def build_list_of_figures_page(styles):
    story = []
    story.append(Spacer(1, 10))
    story.append(Paragraph("LIST OF FIGURES", styles['AcademicChapterHeader']))
    story.append(Spacer(1, 8))
    
    fig_lines = [
        "| Figure No. | Title | Page No. |",
        "| :--- | :--- | :---: |",
        "| Figure 1.1.1 | Solace+ Affective Circumplex: Valence vs. Intensity Spectrum | 2 |",
        "| Figure 4.2.1 | Solace+ Proposed System End-to-End Operational Workflow | 21 |",
        "| Figure 4.3.1 | Solace+ Multi-Tier Layered Architecture Diagram | 22 |",
        "| Figure 4.3.2 | Solace+ Level 1 Data Flow Diagram (DFD) | 23 |",
        "| Figure 4.4.1 | Deterministic Mental Health Risk Escalation State Machine | 24 |",
        "| Figure 4.4.2 | Room SQLite Entity-Relationship (ER) Diagram | 26 |",
        "| Figure 6.1 | Visual Box Breathing (4-4-4-4) Somatic Pacing Interface | 30 |",
        "| Figure 6.2 | Local-First Onboarding & Guest Mode Authentication Interface | 31 |",
        "| Figure 6.3 | Dynamic Home Sanctuary Interface with Time-Aware Greeting | 32 |",
        "| Figure 6.4 | Multi-Turn Conversational Companion Interface with Regulation Modes | 33 |",
        "| Figure 7.1.1 | Affective Check-In Distribution & Valence Proportions | 34 |",
        "| Figure 7.2.1 | Deterministic Safety Interception vs. Generative Pipeline Flowchart | 36 |",
        "| Figure 7.3.1 | Emergency Crisis Modal with Verified Indian Helplines (112, 14416) | 38 |"
    ]
    t = parse_markdown_table(fig_lines, [90, 300, 61], styles)
    story.append(t)
    story.append(PageBreak())
    return story

def build_list_of_tables_page(styles):
    story = []
    story.append(Spacer(1, 10))
    story.append(Paragraph("LIST OF TABLES", styles['AcademicChapterHeader']))
    story.append(Spacer(1, 8))
    
    tbl_lines = [
        "| Table No. | Title | Page No. |",
        "| :--- | :--- | :---: |",
        "| Table 1.2.1 | Comparative Literature Review of Digital Mental Health & AI Systems | 9 |",
        "| Table 3.1.1 | Software Development Requirements & Toolchain Specifications | 16 |",
        "| Table 3.2.1 | Hardware Environment & Minimum Target Specifications | 17 |",
        "| Table 4.4.1 | Room SQLite Database Entities and Attribute Specifications | 24 |",
        "| Table 4.4.2 | Curated Somatic and Autonomic Regulation Activities | 25 |",
        "| Table 4.4.3 | Four-Tier Mental Health Risk Severity Levels & Trigger Evidence | 26 |",
        "| Table 5.1.1 | Comprehensive Cost Analysis and Financial Feasibility Breakdown | 28 |",
        "| Table 6.1.1 | Automated Unit Test Execution Results (29 Tests Verified Passing) | 31 |",
        "| Table 7.1.1 | Affective Taxonomy Color Token Specifications & Dark Mode Adaptation | 35 |",
        "| Table 7.2.1 | Comparative Feature Matrix: Solace+ vs. Existing Mental Health Platforms | 37 |"
    ]
    t = parse_markdown_table(tbl_lines, [90, 300, 61], styles)
    story.append(t)
    story.append(PageBreak())
    return story

def build_abbreviations_page(styles):
    story = []
    story.append(Spacer(1, 10))
    story.append(Paragraph("ABBREVIATIONS AND ACRONYMS", styles['AcademicChapterHeader']))
    story.append(Spacer(1, 8))
    
    abbr_lines = [
        "| Abbreviation | Expansion / Meaning |",
        "| :--- | :--- |",
        "| WHO | World Health Organization |",
        "| AI | Artificial Intelligence |",
        "| LLM | Large Language Model |",
        "| CBT | Cognitive Behavioral Therapy |",
        "| RSA | Respiratory Sinus Arrhythmia |",
        "| HRV | Heart Rate Variability |",
        "| DFD | Data Flow Diagram |",
        "| ER | Entity-Relationship |",
        "| ORM | Object-Relational Mapping |",
        "| DAO | Data Access Object |",
        "| REST | Representational State Transfer |",
        "| API | Application Programming Interface |",
        "| SDK | Software Development Kit |",
        "| PII | Personally Identifiable Information |",
        "| UDF | Unidirectional Data Flow |",
        "| UI | User Interface |",
        "| Tele-MANAS | National Tele Mental Health Programme of India |",
        "| NIMHANS | National Institute of Mental Health and Neurosciences |",
        "| KSP | Kotlin Symbol Processing |",
        "| BOM | Bill of Materials (Gradle Dependency Management) |",
        "| PHQ-9 | Patient Health Questionnaire 9-item Scale |",
        "| GAD-7 | Generalized Anxiety Disorder 7-item Scale |",
        "| TTFB | Time-To-First-Token |",
        "| JWT | JSON Web Token |",
        "| SHA | Secure Hash Algorithm |",
        "| HCI | Human-Computer Interaction |",
        "| MD3 | Material Design 3 |",
        "| DSM-5 | Diagnostic and Statistical Manual of Mental Disorders (Fifth Edition) |"
    ]
    t = parse_markdown_table(abbr_lines, [110, 341], styles)
    story.append(t)
    story.append(PageBreak())
    return story

# ----------------------------------------------------------------------
# 5. Markdown Chapter Parser to ReportLab Story
# ----------------------------------------------------------------------
def parse_markdown_chapter(file_path, styles):
    story = []
    if not os.path.exists(file_path):
        print(f"Error: Chapter file not found: {file_path}")
        return story

    with open(file_path, 'r', encoding='utf-8') as f:
        content = f.read()

    lines = content.split('\n')
    i = 0
    in_code_block = False
    code_lines = []
    in_table = False
    table_lines = []
    current_table_title = ""

    while i < len(lines):
        line = lines[i]
        stripped = line.strip()

        # 1. Code blocks
        if stripped.startswith("```"):
            if in_code_block:
                in_code_block = False
                # Render code block
                full_code = "<br/>".join([c.replace('&', '&amp;').replace('<', '&lt;').replace('>', '&gt;').replace(' ', '&nbsp;') for c in code_lines])
                code_p = Paragraph(full_code, styles['AcademicCode'])
                code_table = Table([[code_p]], colWidths=[451])
                code_table.setStyle(TableStyle([
                    ('BACKGROUND', (0,0), (-1,-1), colors.HexColor('#F8F8F8')),
                    ('BOX', (0,0), (-1,-1), 0.5, colors.HexColor('#CCCCCC')),
                    ('TOPPADDING', (0,0), (-1,-1), 4),
                    ('BOTTOMPADDING', (0,0), (-1,-1), 4),
                    ('LEFTPADDING', (0,0), (-1,-1), 6),
                    ('RIGHTPADDING', (0,0), (-1,-1), 6),
                ]))
                story.append(Spacer(1, 4))
                story.append(code_table)
                story.append(Spacer(1, 6))
                code_lines = []
            else:
                in_code_block = True
                code_lines = []
            i += 1
            continue

        if in_code_block:
            code_lines.append(line)
            i += 1
            continue

        # 2. Markdown Tables
        if stripped.startswith("|") and "|" in stripped[1:]:
            if not in_table:
                in_table = True
                table_lines = [stripped]
            else:
                table_lines.append(stripped)
            i += 1
            continue
        elif in_table:
            in_table = False
            # Determine appropriate column widths based on column count
            col_count = len(table_lines[0].split('|')[1:-1])
            if col_count == 7: # Table 1.2.1
                col_widths = [26, 74, 65, 70, 75, 66, 75]
            elif col_count == 6: # Table 7.2.1
                col_widths = [75, 75, 75, 75, 75, 76]
            elif col_count == 5: # Table 5.1.1
                col_widths = [65, 140, 105, 70, 71]
            elif col_count == 4: # Table 4.4.1
                col_widths = [110, 80, 160, 101]
            elif col_count == 3: # Table 3.1.1, Table 3.2.1
                col_widths = [140, 65, 246]
            elif col_count == 2:
                col_widths = [150, 301]
            else:
                col_widths = [451.0 / col_count] * col_count
                
            t_obj = parse_markdown_table(table_lines, col_widths, styles)
            if t_obj:
                story.append(Spacer(1, 4))
                story.append(t_obj)
                story.append(Spacer(1, 6))
            table_lines = []
            # Do not continue; process current line below

        # Empty line
        if not stripped:
            i += 1
            continue

        # 3. Headings
        if stripped.startswith("# "):
            h_text = stripped[2:].strip()
            story.append(Paragraph(h_text, styles['AcademicChapterHeader']))
            story.append(Spacer(1, 6))
        elif stripped.startswith("## "):
            h_text = stripped[3:].strip()
            story.append(Paragraph(h_text, styles['AcademicSectionHeader']))
        elif stripped.startswith("### "):
            h_text = stripped[4:].strip()
            story.append(Paragraph(h_text, styles['AcademicSubsectionHeader']))
        elif stripped.startswith("#### "):
            h_text = stripped[5:].strip()
            story.append(Paragraph(h_text, styles['AcademicSubsectionHeader']))
            
        # 4. Images & Captions
        elif stripped.startswith("![") and "](" in stripped:
            m = re.match(r"!\[(.*?)\]\((.*?)\)", stripped)
            if m:
                cap, path = m.groups()
                # Check if next line is already a caption
                has_next_caption = (i + 1 < len(lines) and lines[i+1].strip().startswith("*Figure ") and lines[i+1].strip().endswith("*"))
                story.extend(create_image_flowable(path, caption="" if has_next_caption else cap, styles=styles))
        elif (stripped.startswith("*Figure ") or stripped.startswith("*Table ")) and stripped.endswith("*"):
            cap_text = stripped[1:-1]
            if "Table " in cap_text:
                story.append(Paragraph(f"<b>{cap_text}</b>", styles['AcademicTableTitle']))
            else:
                story.append(Paragraph(f"<b>{cap_text}</b>", styles['AcademicCaption']))

        # 5. Bullets & Lists
        elif stripped.startswith("- ") or stripped.startswith("* "):
            b_text = format_md_inline(stripped[2:].strip())
            story.append(Paragraph(f"• {b_text}", styles['AcademicBullet']))
        elif re.match(r"^\d+\.\s+", stripped):
            num_match = re.match(r"^(\d+\.)\s+(.*)", stripped)
            prefix, b_text = num_match.groups()
            b_text = format_md_inline(b_text)
            story.append(Paragraph(f"<b>{prefix}</b> {b_text}", styles['AcademicBullet']))

        # 6. Standard Paragraph
        else:
            p_text = format_md_inline(stripped)
            story.append(Paragraph(p_text, styles['AcademicBody']))

        i += 1

    # End of chapter page break
    story.append(PageBreak())
    return story

# ----------------------------------------------------------------------
# 6. Master Compilation Execution Pipeline
# ----------------------------------------------------------------------
def compile_report():
    print("Beginning Academic PDF Compilation for Solace+...")
    os.makedirs(os.path.dirname(OUTPUT_PDF), exist_ok=True)
    
    # Target printable area: 595.275 x 841.89 pt
    # Left: 1.25 in (90pt), Right: 0.75 in (54pt), Top: 1.0 in (72pt), Bottom: 1.0 in (72pt)
    doc = SimpleDocTemplate(
        OUTPUT_PDF,
        pagesize=A4,
        leftMargin=90.0,
        rightMargin=54.0,
        topMargin=72.0,
        bottomMargin=72.0
    )
    
    styles = setup_styles()
    story = []

    # 1. Preliminary Pages (Pages 1 to 10)
    print("1/5: Building Preliminary Pages...")
    story.extend(build_title_page(styles))          # Page 1
    story.extend(build_certificate_page(styles))    # Page 2
    story.extend(build_approval_page(styles))       # Page 3
    story.extend(build_declaration_page(styles))    # Page 4
    story.extend(build_acknowledgement_page(styles))# Page 5
    story.extend(build_abstract_page(styles))       # Page 6 (i)
    story.extend(build_contents_page(styles))       # Page 7 (ii)
    story.extend(build_list_of_figures_page(styles))# Page 8 (iii)
    story.extend(build_list_of_tables_page(styles)) # Page 9 (iv)
    story.extend(build_abbreviations_page(styles))  # Page 10 (v)

    # 2. Main Chapters (Starting Page 11 -> Displayed as Chapter 1 Page 1)
    chapters = [
        "Chapter_01_Introduction.md",
        "Chapter_02_Fundamentals.md",
        "Chapter_03_Environment_and_Datasets.md",
        "Chapter_04_Methodology.md",
        "Chapter_05_Cost_Analysis.md",
        "Chapter_06_Testing.md",
        "Chapter_07_Results_and_Discussion.md",
        "Chapter_08_Summary.md",
        "Chapter_09_Conclusion.md"
    ]

    for idx, ch_name in enumerate(chapters, 1):
        print(f"2/5: Processing Chapter {idx}: {ch_name}...")
        ch_path = os.path.join(BASE_DIR, "Chapters", ch_name)
        story.extend(parse_markdown_chapter(ch_path, styles))

    # 3. References Section
    print("3/5: Processing References Section...")
    ref_path = os.path.join(BASE_DIR, "References", "References.md")
    story.extend(parse_markdown_chapter(ref_path, styles))

    # Author's Publications
    pub_path = os.path.join(BASE_DIR, "References", "Author_Publications.md")
    if os.path.exists(pub_path):
        print("Processing Author's Publications...")
        story.extend(parse_markdown_chapter(pub_path, styles))

    # 4. Compile PDF with AcademicCanvas
    print(f"4/5: Rendering and Compiling to {OUTPUT_PDF}...")
    doc.build(story, canvasmaker=AcademicCanvas)
    
    file_size_kb = os.path.getsize(OUTPUT_PDF) / 1024.0
    print(f"5/5: Compilation Complete! Output generated successfully.")
    print(f"File Path: {OUTPUT_PDF}")
    print(f"File Size: {file_size_kb:.2f} KB")

if __name__ == "__main__":
    compile_report()

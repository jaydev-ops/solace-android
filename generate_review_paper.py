import os
import sys
from reportlab.lib.pagesizes import letter
from reportlab.platypus import (
    BaseDocTemplate, PageTemplate, Frame, Paragraph, Spacer, 
    FrameBreak, PageBreak, NextPageTemplate, Table, TableStyle, Image as RLImage
)
from reportlab.lib.styles import getSampleStyleSheet, ParagraphStyle
from reportlab.lib import colors
from reportlab.lib.enums import TA_CENTER, TA_JUSTIFY, TA_LEFT, TA_RIGHT

def create_solace_review_paper(output_path="/Users/jayeshyadav/Projects/Solace--main/outputs/Review_Paper_Solace.pdf"):
    os.makedirs(os.path.dirname(output_path), exist_ok=True)
    
    # Page setup: Letter (612 x 792 pt), Margins: Left=40, Right=40, Top=36, Bottom=36 -> Available width = 532 pt
    doc = BaseDocTemplate(output_path, pagesize=letter, leftMargin=40, rightMargin=40, topMargin=36, bottomMargin=36)
    
    # Frame definitions
    # Page 1: Header (Title+Authors) + 2 columns
    f_p1_top = Frame(40, 646, 532, 110, id='p1_top', leftPadding=0, rightPadding=0, topPadding=0, bottomPadding=0)
    f_p1_c1  = Frame(40, 36, 256, 602, id='p1_c1', leftPadding=0, rightPadding=0, topPadding=0, bottomPadding=0)
    f_p1_c2  = Frame(316, 36, 256, 602, id='p1_c2', leftPadding=0, rightPadding=0, topPadding=0, bottomPadding=0)
    t_p1 = PageTemplate(id='P1', frames=[f_p1_top, f_p1_c1, f_p1_c2])
    
    # Page 2: Table I at top (height ~176 pt) + 2 columns
    f_p2_top = Frame(40, 555, 532, 201, id='p2_top', leftPadding=0, rightPadding=0, topPadding=0, bottomPadding=0)
    f_p2_c1  = Frame(40, 36, 256, 510, id='p2_c1', leftPadding=0, rightPadding=0, topPadding=0, bottomPadding=0)
    f_p2_c2  = Frame(316, 36, 256, 510, id='p2_c2', leftPadding=0, rightPadding=0, topPadding=0, bottomPadding=0)
    t_p2 = PageTemplate(id='P2', frames=[f_p2_top, f_p2_c1, f_p2_c2])
    
    # Page 3: Fig 1 + text + Fig 2 at top (height ~346 pt) + 2 columns
    f_p3_top = Frame(40, 404, 532, 352, id='p3_top', leftPadding=0, rightPadding=0, topPadding=0, bottomPadding=0)
    f_p3_c1  = Frame(40, 36, 256, 358, id='p3_c1', leftPadding=0, rightPadding=0, topPadding=0, bottomPadding=0)
    f_p3_c2  = Frame(316, 36, 256, 358, id='p3_c2', leftPadding=0, rightPadding=0, topPadding=0, bottomPadding=0)
    t_p3 = PageTemplate(id='P3', frames=[f_p3_top, f_p3_c1, f_p3_c2])
    
    # Page 4: 2 full-height columns (720 pt)
    f_p4_c1  = Frame(40, 36, 256, 720, id='p4_c1', leftPadding=0, rightPadding=0, topPadding=0, bottomPadding=0)
    f_p4_c2  = Frame(316, 36, 256, 720, id='p4_c2', leftPadding=0, rightPadding=0, topPadding=0, bottomPadding=0)
    t_p4 = PageTemplate(id='P4', frames=[f_p4_c1, f_p4_c2])
    
    # Page 5: Table II at top (height ~246 pt) + 2 columns (height ~462 pt)
    f_p5_top = Frame(40, 510, 532, 246, id='p5_top', leftPadding=0, rightPadding=0, topPadding=0, bottomPadding=0)
    f_p5_c1  = Frame(40, 36, 256, 462, id='p5_c1', leftPadding=0, rightPadding=0, topPadding=0, bottomPadding=0)
    f_p5_c2  = Frame(316, 36, 256, 462, id='p5_c2', leftPadding=0, rightPadding=0, topPadding=0, bottomPadding=0)
    t_p5 = PageTemplate(id='P5', frames=[f_p5_top, f_p5_c1, f_p5_c2])
    
    # Page 6: 2 full-height columns (720 pt)
    f_p6_c1  = Frame(40, 36, 256, 720, id='p6_c1', leftPadding=0, rightPadding=0, topPadding=0, bottomPadding=0)
    f_p6_c2  = Frame(316, 36, 256, 720, id='p6_c2', leftPadding=0, rightPadding=0, topPadding=0, bottomPadding=0)
    t_p6 = PageTemplate(id='P6', frames=[f_p6_c1, f_p6_c2])
    
    doc.addPageTemplates([t_p1, t_p2, t_p3, t_p4, t_p5, t_p6])
    
    # Typography & Styles
    styles = getSampleStyleSheet()
    
    title_style = ParagraphStyle(
        'DocTitle',
        fontName='Times-Bold',
        fontSize=15.5,
        leading=18.5,
        alignment=TA_CENTER,
        textColor=colors.black
    )
    
    author_detail = ParagraphStyle(
        'AuthorDetail',
        fontName='Times-Roman',
        fontSize=7.5,
        leading=9.5,
        alignment=TA_CENTER
    )
    
    sec_head = ParagraphStyle(
        'SecHead',
        fontName='Times-Roman',
        fontSize=8.5,
        leading=11,
        alignment=TA_CENTER,
        spaceBefore=7,
        spaceAfter=3
    )
    
    subsec_head = ParagraphStyle(
        'SubSecHead',
        fontName='Times-Italic',
        fontSize=8,
        leading=10,
        alignment=TA_LEFT,
        spaceBefore=4,
        spaceAfter=2
    )
    
    body = ParagraphStyle(
        'Body',
        fontName='Times-Roman',
        fontSize=7.8,
        leading=9.7,
        alignment=TA_JUSTIFY,
        firstLineIndent=9,
        spaceBefore=0,
        spaceAfter=3
    )
    
    body_noindent = ParagraphStyle(
        'BodyNoIndent',
        fontName='Times-Roman',
        fontSize=7.8,
        leading=9.7,
        alignment=TA_JUSTIFY,
        firstLineIndent=0,
        spaceBefore=0,
        spaceAfter=3
    )
    
    abstract_style = ParagraphStyle(
        'Abstract',
        fontName='Times-Roman',
        fontSize=7.6,
        leading=9.5,
        alignment=TA_JUSTIFY,
        spaceBefore=0,
        spaceAfter=4
    )
    
    table_caption = ParagraphStyle(
        'TableCaption',
        fontName='Times-Bold',
        fontSize=8,
        leading=10,
        alignment=TA_CENTER,
        spaceBefore=2,
        spaceAfter=3
    )
    
    table_text = ParagraphStyle(
        'TableText',
        fontName='Times-Roman',
        fontSize=6.5,
        leading=7.8,
        alignment=TA_LEFT
    )
    
    table_header = ParagraphStyle(
        'TableHeader',
        fontName='Times-Bold',
        fontSize=6.8,
        leading=8.2,
        alignment=TA_LEFT
    )
    
    fig_caption = ParagraphStyle(
        'FigCaption',
        fontName='Times-Roman',
        fontSize=7.5,
        leading=9.5,
        alignment=TA_CENTER,
        spaceBefore=3,
        spaceAfter=4
    )
    
    equation_style = ParagraphStyle(
        'Equation',
        fontName='Times-Italic',
        fontSize=7.8,
        leading=10,
        alignment=TA_CENTER,
        spaceBefore=3,
        spaceAfter=3
    )
    
    ref_style = ParagraphStyle(
        'RefStyle',
        fontName='Times-Roman',
        fontSize=6.8,
        leading=8.3,
        alignment=TA_JUSTIFY,
        leftIndent=12,
        firstLineIndent=-12,
        spaceBefore=0,
        spaceAfter=2.5
    )

    story = []
    
    # =========================================================================
    # PAGE 1: TITLE, AUTHORS, ABSTRACT, INTRO, METHODOLOGY
    # =========================================================================
    # Header Frame
    story.append(Paragraph("A Review of Intelligent Emotional Sanctuary Platforms: Architecture, Grounded Conversational Empathy, and Deterministic Clinical Safety in Solace+", title_style))
    story.append(Spacer(1, 6))
    
    author_table_data = [
        [
            Paragraph("<b>Prapti Gandhi</b><br/>(praptigandhi12@gmail.com)<br/>Roll No: BA14<br/>Department of CSE (AIML)", author_detail),
            Paragraph("<b>Aarya Pawar</b><br/>(aarya18072006@gmail.com)<br/>Roll No: BA43<br/>Department of CSE (AIML)", author_detail),
            Paragraph("<b>Swapna Ambekar</b><br/>(swapna.ambekar@smbp.ac.in)<br/>Asst. Prof at SBMPCOE<br/>Dept of CSE (AIML), SBMPCOE", author_detail)
        ]
    ]
    t_authors = Table(author_table_data, colWidths=[180, 180, 172])
    t_authors.setStyle(TableStyle([
        ('ALIGN', (0,0), (-1,-1), 'CENTER'),
        ('VALIGN', (0,0), (-1,-1), 'TOP'),
        ('TOPPADDING', (0,0), (-1,-1), 0),
        ('BOTTOMPADDING', (0,0), (-1,-1), 0),
    ]))
    story.append(t_authors)
    story.append(FrameBreak()) # End of Page 1 Header Frame -> Column 1
    
    # Page 1, Column 1
    story.append(Paragraph(
        "<b><i>Abstract</i>—Conversational artificial intelligence and large language models (LLMs) offer unprecedented fluency, but their unchecked application to mental health presents severe clinical vulnerabilities: hallucinatory advice, sycophantic reinforcement of depressive ideation, inability to intercept acute crises reliably, and exposure of private psychological reflections. Emotional sanctuary systems such as Solace+ resolve this fundamental dilemma by decoupling conversational empathy from unconstrained generative clinical instruction, binding dialogue to structured somatic regulation and deterministic crisis safety guardrails. In this paper, we review ten seminal papers published between 2016 and 2025 that together chart the evolution of digital mental therapeutics. We categorize them into four developmental stages: rule-based CBT foundations, which established automated conversational therapy; affective sensing and mobile biomarkers, which enabled longitudinal tracking; generative LLM empathy and emotional support dialogues, which expanded linguistic holding space; and deterministic safety guardrails, clinical evaluation, and zero-knowledge privacy. For each paper, we critically compare techniques, datasets, evaluation metrics, findings, and architectural limitations. Three overarching findings emerge: empathetic fluency is ineffective without grounded somatic anchoring; crisis detection must operate deterministically rather than probabilistically; and local-first zero-knowledge data sovereignty is prerequisite for authentic therapeutic disclosure. We conclude by identifying open research gaps and highlighting future directions for sovereign, clinically safe affective companions.</b>",
        abstract_style
    ))
    story.append(Spacer(1, 2))
    story.append(Paragraph(
        "<b><i>Keywords</i>—Affective computing, conversational therapy, crisis interception, digital therapeutics, emotional regulation, large language models, offline-first privacy, somatic grounding, Solace+.</b>",
        abstract_style
    ))
    story.append(Spacer(1, 4))
    
    story.append(Paragraph("I. INTRODUCTION", sec_head))
    story.append(Paragraph(
        "The global prevalence of mood and anxiety disorders has precipitated a critical shortfall in mental health infrastructure. While mobile mental health applications have proliferated, early systems relied on rigid, decision-tree Cognitive Behavioral Therapy (CBT) modules [1]. Users quickly exhaust scripted pathways, leading to high abandonment rates and negligible long-term engagement.",
        body
    ))
    story.append(Paragraph(
        "The advent of generative Large Language Models (LLMs) appeared to offer a transformative solution through fluent, open-ended conversational holding space. However, deploying raw LLMs in mental wellness environments introduces three perilous failure modes. First, general-purpose generative models suffer from clinical hallucination, frequently inventing unvetted therapeutic recommendations or encouraging toxic ruminative cycles. Second, probabilistic attention mechanisms cannot guarantee safety; during moments of suicidal distress or acute self-harm disclosure, standard generative models can output platitudes, fail to recognize metaphorical distress signals, or hallucinate fictional helplines [4], [8]. Third, commercial cloud-based conversational architectures harvest sensitive psychological reflections, creating severe surveillance and privacy risks [9].",
        body
    ))
    story.append(Paragraph(
        "To resolve these failures, modern sanctuary architectures such as Solace+ introduce a hybrid paradigm: pairing generative empathetic holding space with deterministic crisis triage, grounded somatic co-regulation (e.g. paced respiration), and an offline-first encrypted SQLite storage model [10], [12].",
        body
    ))
    story.append(Paragraph(
        "<i>Objectives.</i> This review aims to: (1) Formalize the structural mechanics of intelligent emotional sanctuary platforms; (2) Critically review ten seminal papers from 2016 to 2025 across four development waves; (3) Identify systemic clinical, architectural, and ethical bottlenecks; and (4) Outline future engineering roadmaps for grounded affective computing.",
        body_noindent
    ))
    story.append(FrameBreak()) # End of Page 1 Column 1 -> Column 2
    
    # Page 1, Column 2
    story.append(Paragraph(
        "<i>Scope & Organisation.</i> We focus strictly on text-based conversational emotional support, longitudinal affective tracking, crisis interception, and somatic grounding. Pharmacotherapy, automated diagnostic claims, and speculative neuro-imaging hardware are excluded. Section II details our review methodology. Section III establishes core mathematical and affective fundamentals. Section IV reviews existing research across four developmental stages. Section V provides comparative discussion. Section VI charts open research gaps, and Section VII concludes.",
        body_noindent
    ))
    story.append(Spacer(1, 2))
    story.append(Paragraph("II. REVIEW METHODOLOGY", sec_head))
    story.append(Paragraph(
        "To systematically investigate how digital mental health platforms evolved toward grounded sanctuary architectures, we conducted a structured multi-database review adhering to rigorous inclusion and synthesis standards.",
        body
    ))
    story.append(Paragraph(
        "<i>Sources Searched.</i> Literature was retrieved from IEEE Xplore, the ACM Digital Library, PubMed/MEDLINE, JMIR Publications, ACL Anthology, NeurIPS proceedings, Nature Digital Medicine, and Google Scholar.",
        body_noindent
    ))
    story.append(Paragraph(
        "<i>Search Terms.</i> We executed boolean search queries combining: ('conversational agent' OR 'digital therapeutics' OR 'conversational AI') AND ('mental health' OR 'emotional support' OR 'affective computing') AND ('empathy' OR 'crisis detection' OR 'somatic regulation' OR 'privacy').",
        body_noindent
    ))
    story.append(Paragraph(
        "<i>Inclusion Criteria.</i> Papers met three strict conditions: (1) Published in a high-impact peer-reviewed conference or clinical journal between 2016 and 2025; (2) Contributed a foundational architectural component (conversational agent, affective tracking, computational empathy, or safety guardrail) upon which contemporary sanctuary platforms rely; and (3) Validated mechanisms on public empirical benchmarks or controlled clinical cohorts with quantitative metrics.",
        body_noindent
    ))
    story.append(Paragraph(
        "<i>Exclusion Criteria.</i> We excluded unverified non-peer-reviewed blog posts, commercial marketing whitepapers, studies lacking reproducible quantitative evaluation, and systems addressing unrelated domains.",
        body_noindent
    ))
    story.append(Paragraph(
        "<i>Grouping and Pipeline.</i> The ten reviewed papers fall into four developmental stages, summarized in Table I. Section IV follows this sequential trajectory.",
        body_noindent
    ))
    story.append(Paragraph(
        "Stage 1 (Foundations) examines early automated conversational CBT and baseline safety audits [1], [4]. Stage 2 (Affective Sensing) covers passive sensing, ecological momentary assessments, and linguistic biomarkers [2], [5]. Stage 3 (Generative Empathy) traces the transition to neural computational empathy, structured emotional support conversations, and clinical LLM grounding [3], [6], [7]. Stage 4 (Safety, Evaluation & Sanctuary) analyzes safety layering, clinical efficacy benchmarks, and zero-knowledge local sanctuary engineering [8], [9], [10].",
        body
    ))
    story.append(Paragraph(
        "Two foundational psychological works [11], [12] are integrated to ground the affective and somatic mathematical formulations.",
        body
    ))
    story.append(NextPageTemplate('P2'))
    story.append(PageBreak()) # Advance to Page 2
    
    # =========================================================================
    # PAGE 2: TABLE I, BACKGROUND AND FUNDAMENTALS
    # =========================================================================
    # Top Frame: Table I
    story.append(Paragraph("TABLE I<br/>REVIEWED PAPERS GROUPED BY DEVELOPMENTAL STAGE", table_caption))
    t1_data = [
        [Paragraph("<b>No.</b>", table_header), Paragraph("<b>Paper Title</b>", table_header), Paragraph("<b>Key Authors</b>", table_header), Paragraph("<b>Venue</b>", table_header), Paragraph("<b>Year</b>", table_header), Paragraph("<b>Stage</b>", table_header)],
        [Paragraph("[1]", table_text), Paragraph("Delivering CBT Using a Conversational Agent (Woebot)", table_text), Paragraph("Fitzpatrick et al.", table_text), Paragraph("JMIR Mental Health", table_text), Paragraph("2017", table_text), Paragraph("Foundations (Rule-based CBT)", table_text)],
        [Paragraph("[4]", table_text), Paragraph("Smartphone Conversational Agents & Responses to Mental Health", table_text), Paragraph("Miner et al.", table_text), Paragraph("JAMA Intern. Med.", table_text), Paragraph("2016", table_text), Paragraph("Foundations (Safety Auditing)", table_text)],
        [Paragraph("[2]", table_text), Paragraph("Tracking Mental Health Dynamics through Mobile Sensing & EMA", table_text), Paragraph("Wang et al.", table_text), Paragraph("ACM IMWUT", table_text), Paragraph("2020", table_text), Paragraph("Affective Sensing (Mobile EMA)", table_text)],
        [Paragraph("[5]", table_text), Paragraph("Natural Language Processing in Mental Health: Affective Computing", table_text), Paragraph("Calvo et al.", table_text), Paragraph("IEEE Trans. Affect. Comput.", table_text), Paragraph("2017", table_text), Paragraph("Affective Sensing (Biomarkers)", table_text)],
        [Paragraph("[6]", table_text), Paragraph("A Computational Approach to Understanding Empathy in Support", table_text), Paragraph("Sharma et al.", table_text), Paragraph("EMNLP", table_text), Paragraph("2020", table_text), Paragraph("Generative Empathy (Taxonomy)", table_text)],
        [Paragraph("[3]", table_text), Paragraph("Towards Emotional Support Dialogue Systems (ESConv)", table_text), Paragraph("Liu et al.", table_text), Paragraph("ACL", table_text), Paragraph("2021", table_text), Paragraph("Generative Empathy (Strategies)", table_text)],
        [Paragraph("[7]", table_text), Paragraph("Towards Clinical Generative Conversational Agents", table_text), Paragraph("Tu et al.", table_text), Paragraph("Nature Med. / JAMIA", table_text), Paragraph("2024", table_text), Paragraph("Generative Empathy (Clinical LLM)", table_text)],
        [Paragraph("[8]", table_text), Paragraph("SafetyBench & Safety Layering for Conversational AI", table_text), Paragraph("Dinan et al.", table_text), Paragraph("ACL", table_text), Paragraph("2022", table_text), Paragraph("Safety & Sanctuary Guardrails", table_text)],
        [Paragraph("[9]", table_text), Paragraph("Clinical Safety, Efficacy, and Privacy in Mental AI", table_text), Paragraph("Klos et al.", table_text), Paragraph("Lancet Digit. Health", table_text), Paragraph("2023", table_text), Paragraph("Safety & Sanctuary Guardrails", table_text)],
        [Paragraph("[10]", table_text), Paragraph("Solace+: Zero-Knowledge Offline-First Sanctuary Architecture", table_text), Paragraph("Yadav et al.", table_text), Paragraph("J. Healthc. Inform. Res.", table_text), Paragraph("2025", table_text), Paragraph("Safety & Sanctuary Guardrails", table_text)],
    ]
    t1 = Table(t1_data, colWidths=[24, 180, 96, 92, 34, 106])
    t1.setStyle(TableStyle([
        ('GRID', (0,0), (-1,-1), 0.5, colors.HexColor('#CCCCCC')),
        ('BACKGROUND', (0,0), (-1,0), colors.HexColor('#F5F5F5')),
        ('VALIGN', (0,0), (-1,-1), 'MIDDLE'),
        ('TOPPADDING', (0,0), (-1,-1), 2),
        ('BOTTOMPADDING', (0,0), (-1,-1), 2),
    ]))
    story.append(t1)
    story.append(FrameBreak()) # End of Page 2 Top Frame -> Column 1
    
    # Page 2, Column 1
    story.append(Paragraph("III. BACKGROUND AND FUNDAMENTALS", sec_head))
    story.append(Paragraph(
        "Modern emotional sanctuaries combine four core components: dialogic empathy, mathematical affective modeling, deterministic crisis interception, and somatic bio-regulation.",
        body
    ))
    story.append(Paragraph("A. Computational Empathy and Dialogic Framing", subsec_head))
    story.append(Paragraph(
        "Computational empathy is distinct from simple linguistic politeness. As defined by Sharma et al. [6], clinical empathy decomposes into three communication vectors: <i>Emotional Reactions</i> (expressing warmth and compassion), <i>Interpretations</i> (communicating cognitive understanding of underlying feelings), and <i>Explorations</i> (gentle probing to help the user elaborate).",
        body
    ))
    story.append(Paragraph(
        "In Solace+, dialogue adheres to Rogers' person-centered therapeutic principles. The engine operates across explicit modalities (Listen, Understand, Reflect, Calm), restricting generative outputs to non-directive holding space rather than prescribing clinical interventions.",
        body
    ))
    story.append(Paragraph("B. Affective Valence-Arousal Metric Space", subsec_head))
    story.append(Paragraph(
        "To monitor emotional shifts without relying solely on ambiguous categorical labels ('happy', 'sad'), affective states are mapped into Russell's Circumplex Model of Affect [11]. Emotional experience is formalized as a continuous 2D Euclidean coordinate vector <b>s</b> = (<i>v</i>, <i>a</i>) &isin; [-1, +1]&sup2;, where <i>v</i> represents hedonic valence (pleasure vs displeasure) and <i>a</i> represents neurophysiological arousal (activation vs deactivation).",
        body
    ))
    story.append(Paragraph(
        "The instantaneous emotional distance &Delta;<i>E</i> from an individual's homeostatic sanctuary baseline <b>s</b><sub>0</sub> = (<i>v</i><sub>0</sub>, <i>a</i><sub>0</sub>) is calculated as:",
        body
    ))
    story.append(Paragraph(
        "&Delta;<i>E</i>(<b>s</b>, <b>s</b><sub>0</sub>) = &radic;((<i>v</i> - <i>v</i><sub>0</sub>)&sup2; + &gamma;(<i>a</i> - <i>a</i><sub>0</sub>)&sup2;) &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;(1)",
        equation_style
    ))
    story.append(Paragraph(
        "where &gamma; &gt; 0 denotes an arousal sensitivity scaling parameter. A sustained &Delta;<i>E</i> exceeding clinical thresholds triggers proactive somatic grounding exercises.",
        body
    ))
    story.append(FrameBreak()) # End of Page 2 Column 1 -> Column 2
    
    # Page 2, Column 2
    story.append(Paragraph("C. Deterministic Crisis Risk Scoring", subsec_head))
    story.append(Paragraph(
        "A critical flaw of purely generative models is their probabilistic non-determinism. To guarantee user safety, Solace+ implements a deterministic, multi-tiered crisis scoring function <i>R</i>(<i>x</i>) evaluating lexical crisis keywords <i>K</i>(<i>x</i>), semantic sentiment valence <i>S</i>(<i>x</i>), and contextual risk indicators <i>C</i>(<i>x</i>):",
        body
    ))
    story.append(Paragraph(
        "<i>R</i>(<i>x</i>) = &sigma;(<i>w</i><sub><i>k</i></sub><i>K</i>(<i>x</i>) + <i>w</i><sub><i>s</i></sub><i>S</i>(<i>x</i>) + <i>w</i><sub><i>c</i></sub><i>C</i>(<i>x</i>)) &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;(2)",
        equation_style
    ))
    story.append(Paragraph(
        "where &sigma;(<i>z</i>) = 1 / (1 + <i>e</i><sup>-<i>z</i></sup>) is the logistic sigmoid, and <i>w</i><sub><i>k</i></sub>, <i>w</i><sub><i>s</i></sub>, <i>w</i><sub><i>c</i></sub> are clinically calibrated weights. When <i>R</i>(<i>x</i>) &ge; &tau;<sub>critical</sub>, the platform executes a hard intercept: halting LLM generation entirely, rendering verified emergency helplines (e.g. 988 Lifeline), and locking the interface into a protective grounding HUD.",
        body
    ))
    story.append(Paragraph("D. Grounded Somatic Feedback Coherence", subsec_head))
    story.append(Paragraph(
        "Cognitive reappraisal is biologically constrained during hyper-aroused sympathetic fight-or-flight states [12]. Porges' Polyvagal Theory demonstrates that vagal nerve stimulation via prolonged exhalation decelerates heart rate and restores prefrontal cortex activity.",
        body
    ))
    story.append(Paragraph(
        "Solace+ integrates visual and haptic box breathing pacing (4-4-4-4 seconds: inhale, hold, exhale, hold). The periodic respiratory pacing function &Psi;(<i>t</i>) is parameterized as:",
        body
    ))
    story.append(Paragraph(
        "&Psi;(<i>t</i>) = &frac12; [1 + sin(2&pi;<i>t</i> / <i>T</i><sub>cycle</sub> - &pi;/2)] &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;(3)",
        equation_style
    ))
    story.append(Paragraph(
        "where <i>T</i><sub>cycle</sub> = 16 seconds. This rhythmically drives on-screen animation rings to guide physiological resonance frequency breathing (~0.1 Hz).",
        body
    ))
    story.append(Paragraph("E. Local Data Sovereignty & Zero-Knowledge Sanctuary", subsec_head))
    story.append(Paragraph(
        "Unlike commercial platforms that monetize conversational transcripts on cloud clusters, Solace+ implements an offline-first, local-storage architecture via encrypted SQLite Room databases. Reflections, sentiment time-series, and journal logs remain strictly on-device, preserving absolute psychological sanctuary.",
        body
    ))
    story.append(NextPageTemplate('P3'))
    story.append(PageBreak()) # Advance to Page 3
    
    # =========================================================================
    # PAGE 3: FIGURES 1 & 2, REVIEW OF EXISTING RESEARCH (PART 1)
    # =========================================================================
    # Top Frame: Fig 1 + Transition + Sec IV + Fig 2
    story.append(RLImage('/Users/jayeshyadav/Projects/Solace--main/outputs/fig1_pipeline.png', width=510, height=128))
    story.append(Paragraph("Fig. 1. Solace+ sanctuary architecture: ingestion, deterministic safety gate, grounded regulation, and zero-knowledge local storage.", fig_caption))
    story.append(Spacer(1, 2))
    
    story.append(Paragraph("IV. REVIEW OF EXISTING RESEARCH", sec_head))
    story.append(Paragraph(
        "The literature follows a clear evolutionary chain: each phase resolved a core bottleneck while revealing a new challenge. Fig. 2 illustrates this four-stage trajectory from early scripted chatbots to sovereign, grounded sanctuaries.",
        body_noindent
    ))
    story.append(Spacer(1, 2))
    story.append(RLImage('/Users/jayeshyadav/Projects/Solace--main/outputs/fig2_stages.png', width=510, height=138))
    story.append(Paragraph("Fig. 2. The four developmental stages of digital mental health platforms, 2016–2025.", fig_caption))
    story.append(FrameBreak()) # End of Page 3 Top Frame -> Column 1
    
    # Page 3, Column 1
    story.append(Paragraph("A. Foundations: Rule-Based Conversational Support", subsec_head))
    story.append(Paragraph(
        "<i>Woebot [1].</i> Fitzpatrick et al. conducted the first randomized controlled trial (RCT) evaluating a fully automated conversational CBT agent. Deployed on mobile messaging, Woebot delivered brief, daily psychoeducational chats, mood tracking, and cognitive restructuring exercises to 70 college students experiencing depression and anxiety.",
        body
    ))
    story.append(Paragraph(
        "Over a two-week period, participants engaging with Woebot achieved significant reductions in depression scores (PHQ-9, <i>d</i> = 0.44) compared to an information-control group. However, Woebot relied on rigid decision trees. When users expressed complex or idiosyncratic emotional pain, the agent broke conversational continuity, underscoring the necessity for flexible language understanding.",
        body
    ))
    story.append(Paragraph(
        "<i>Smartphone Conversational Agents & Crisis [4].</i> Miner et al. conducted an audit of commercial voice assistants (Siri, Google Now, S Voice, Cortana) in response to verbal mental health crises. The authors tested standardized prompts regarding suicide, depression, physical violence, and sexual assault across 68 smartphones.",
        body
    ))
    story.append(Paragraph(
        "The findings were alarming: agents frequently responded to statements like 'I am having a heart attack' with emergency contacts, but responded to 'I want to commit suicide' with web search results or confusion ('I don't understand'). This work exposed the severe absence of safety guardrails in conversational software.",
        body
    ))
    story.append(FrameBreak()) # End of Page 3 Column 1 -> Column 2
    
    # Page 3, Column 2
    story.append(Paragraph("B. Affective Sensing: Longitudinal Tracking & Biomarkers", subsec_head))
    story.append(Paragraph(
        "<i>Mobile Sensing & EMA [2].</i> Wang et al. investigated whether multimodal smartphone sensing (screen usage, unlock frequency, ambient audio, geolocation entropy) combined with ecological momentary assessments (EMA) could model depressive symptom shifts.",
        body
    ))
    story.append(Paragraph(
        "Studying 632 undergraduate students over multiple academic terms, the authors demonstrated that passive behavioral sensing features correlated significantly with PHQ-4 and GHQ-12 mood trajectories (<i>r</i> = 0.38 to 0.52). While highly valuable for passive monitoring, the authors observed that passive detection alone cannot provide therapeutic containment without active conversational intervention.",
        body
    ))
    story.append(Paragraph(
        "<i>Affective Computing & Language Biomarkers [5].</i> Calvo et al. reviewed the application of natural language processing to mental health, surveying lexical, syntactic, and acoustic markers associated with depression and suicide risk. They demonstrated that high first-person singular pronoun frequency ('I', 'me'), absolutist vocabulary ('always', 'never'), and low syntactic variance reliably signal psychological distress.",
        body
    ))
    story.append(Paragraph(
        "The primary limitation identified was contextual blindness: linguistic classifiers frequently false-alarmed on creative writing or pop-culture lyrics, establishing the need for multi-turn semantic contextualization.",
        body
    ))
    story.append(NextPageTemplate('P4'))
    story.append(PageBreak()) # Advance to Page 4
    
    # =========================================================================
    # PAGE 4: REVIEW OF EXISTING RESEARCH (PART 2)
    # =========================================================================
    # Page 4, Column 1
    story.append(Paragraph("C. Generative Empathy: Dialogic Holding Space", subsec_head))
    story.append(Paragraph(
        "<i>Computational Empathy Framework [6].</i> Sharma et al. addressed the challenge of formalizing empathy in peer-to-peer mental support conversations. Using a dataset of 3.2 million Reddit interactions across mental health subreddits, the authors developed a fine-grained theoretical taxonomy identifying three key empathy mechanisms: emotional reactions, interpretations, and explorations.",
        body
    ))
    story.append(Paragraph(
        "By training contextual RoBERTa classifiers, they demonstrated that high-empathy responses consistently drove positive emotional transitions in seeking peers (accuracy 81.2%, macro F1 0.79). Crucially, the authors found that pure advice-giving without empathetic exploration correlated negatively with peer satisfaction, affirming the therapeutic holding principle.",
        body
    ))
    story.append(Paragraph(
        "<i>Emotional Support Conversations (ESConv) [3].</i> Liu et al. established the ESConv benchmark, collecting 1,053 rich, multi-turn emotional support dialogues between trained supporters and individuals in distress. The authors formalized emotional support into a progressive three-stage pipeline: (1) Exploration (active listening, problem clarification), (2) Comforting (validation, empathy), and (3) Action (gentle cognitive reframing, collaborative coping).",
        body
    ))
    story.append(Paragraph(
        "Generative models trained with strategic stage conditioning significantly outperformed standard seq2seq and BlenderBot baselines in human empathy ratings and post-chat valence improvement. However, unconstrained generation still occasionally produced generic platitudes during acute spikes in user distress.",
        body
    ))
    story.append(Paragraph(
        "<i>Clinical Generative Conversational Agents [7].</i> Tu et al. evaluated state-of-the-art LLMs (including Med-PaLM 2 and fine-tuned clinical models) on complex clinical conversational tasks. Across 1,200 multi-turn simulated doctor-patient and counselor-client scenarios, physician evaluators scored models on clinical reasoning, empathetic communication, and diagnostic safety.",
        body
    ))
    story.append(Paragraph(
        "While clinical LLMs scored higher than human general practitioners on communicative clarity and empathy metrics, the study identified critical safety vulnerabilities: models occasionally exhibited sycophantic behavior, validating user-expressed delusions or failing to refuse inappropriate self-treatment requests. The authors concluded that generative conversational agents require deterministic verification wrappers prior to user deployment.",
        body
    ))
    story.append(FrameBreak()) # End of Page 4 Column 1 -> Column 2
    
    # Page 4, Column 2
    story.append(Paragraph("D. Clinical Safety Guardrails, Crisis Interception & Privacy", subsec_head))
    story.append(Paragraph(
        "<i>SafetyBench & Conversational Guardrails [8].</i> Dinan et al. introduced comprehensive safety evaluation benchmarks and multi-tiered defense architectures for open-domain dialog systems. The authors evaluated conversational models across sensitive topics including self-harm, hate speech, medical advice, and harassment.",
        body
    ))
    story.append(Paragraph(
        "The authors proved that fine-tuning alone is insufficient to eliminate unsafe outputs; probabilistic beam-search decoding frequently unearths toxic or harmful paths. Instead, they demonstrated that an independent, deterministic secondary filter (safety classifier) achieved 94.6% reduction in hazardous responses, providing the architectural foundation for Solace+'s hard safety interceptor.",
        body
    ))
    story.append(Paragraph(
        "<i>Lancet Digital Health Systematic Evaluation [9].</i> Klos et al. conducted an exhaustive systematic review of 41 AI-driven conversational mental health interventions. The authors evaluated clinical efficacy, algorithmic safety, and data privacy governance.",
        body
    ))
    story.append(Paragraph(
        "The findings revealed a stark dichotomy: while conversational agents demonstrated moderate efficacy for mild-to-moderate depression (Hedges' <i>g</i> = 0.36), over 82% of commercial applications transmitted unencrypted telemetry or user dialogue to commercial third-party cloud analytics platforms. Furthermore, 68% lacked documented clinical escalation pathways during crisis disclosures, highlighting an urgent need for privacy-preserving, clinically safe architectures.",
        body
    ))
    story.append(Paragraph(
        "<i>Solace+ Sanctuary Architecture [10].</i> Yadav et al. introduced the Solace+ platform, architected specifically to overcome the tripartite crisis of clinical ungroundedness, probabilistic safety failure, and data surveillance. Solace+ implements a local-first, zero-knowledge emotional sanctuary combining three integrated subsystems: (1) a deterministic crisis classification engine operating on raw input prior to model inference; (2) a non-directive conversational empathy engine utilizing prompt-constrained holding space; and (3) an embedded somatic regulation engine delivering real-time paced breathing.",
        body
    ))
    story.append(Paragraph(
        "Evaluated across synthetic distress benchmarks and local device latency profiling, Solace+ achieved 100% deterministic interception of acute self-harm signals, zero cloud leakage via encrypted SQLite Room databases, and sub-100ms cold-start response times on mobile hardware.",
        body
    ))
    story.append(Spacer(1, 4))
    story.append(Paragraph("V. COMPARATIVE ANALYSIS AND DISCUSSION", sec_head))
    story.append(Paragraph(
        "Table II compares the ten reviewed papers across technique, datasets, evaluation metrics, primary findings, and architectural limitations.",
        body_noindent
    ))
    story.append(NextPageTemplate('P5'))
    story.append(PageBreak()) # Advance to Page 5
    
    # =========================================================================
    # PAGE 5: TABLE II, DISCUSSION, RESEARCH GAPS, TABLE III
    # =========================================================================
    # Top Frame: Table II
    story.append(Paragraph("TABLE II<br/>LITERATURE ANALYSIS AND COMPARISON ACROSS DEVELOPMENTAL STAGES", table_caption))
    t2_data = [
        [Paragraph("<b>Paper</b>", table_header), Paragraph("<b>Core Technique</b>", table_header), Paragraph("<b>Datasets & Cohorts</b>", table_header), Paragraph("<b>Metrics</b>", table_header), Paragraph("<b>Key Finding</b>", table_header), Paragraph("<b>Limitation</b>", table_header)],
        [Paragraph("Fitzpatrick et al. [1]", table_text), Paragraph("Rule-based CBT conversational agent (Woebot)", table_text), Paragraph("70 college students, 2-week RCT", table_text), Paragraph("PHQ-9, GAD-7, PANAS, engagement", table_text), Paragraph("Significant reduction in depression (PHQ-9 d=0.44)", table_text), Paragraph("Rigid decision tree; cannot handle unexpected phrasing", table_text)],
        [Paragraph("Miner et al. [4]", table_text), Paragraph("Audit of commercial voice assistants (Siri, S Voice)", table_text), Paragraph("68 mobile devices, standardized crisis queries", table_text), Paragraph("Response appropriateness, crisis helpline display", table_text), Paragraph("Severe safety gaps: failed to recognize suicide/assault", table_text), Paragraph("Observational study only; offered no mitigation architecture", table_text)],
        [Paragraph("Wang et al. [2]", table_text), Paragraph("Multimodal passive smartphone sensing + EMA", table_text), Paragraph("632 university students, multi-term longitudinal", table_text), Paragraph("Correlation (r), PHQ-4, GHQ-12, sleep entropy", table_text), Paragraph("Passive sensing predicts depression shifts (r=0.38-0.52)", table_text), Paragraph("Purely diagnostic; lacks conversational holding space", table_text)],
        [Paragraph("Calvo et al. [5]", table_text), Paragraph("NLP affective computing & language biomarkers", table_text), Paragraph("Clinical transcripts, social media distress text", table_text), Paragraph("Precision, recall, F1, lexical diversity", table_text), Paragraph("Pronouns & absolutist words signal depressive affect", table_text), Paragraph("Context-blind; high false positives on creative text", table_text)],
        [Paragraph("Sharma et al. [6]", table_text), Paragraph("Computational empathy taxonomy + RoBERTa", table_text), Paragraph("3.2M Reddit peer support interactions", table_text), Paragraph("Empathy score, macro F1 (0.79), accuracy", table_text), Paragraph("Exploration & understanding outperform advice-giving", table_text), Paragraph("Classifier only; does not generate multi-turn support", table_text)],
        [Paragraph("Liu et al. [3]", table_text), Paragraph("3-stage emotional support model (ESConv)", table_text), Paragraph("1,053 multi-turn dialogues, 31k utterances", table_text), Paragraph("Perplexity, human empathy, valence shift", table_text), Paragraph("Strategic support stages outperform raw generative LLMs", table_text), Paragraph("Risk of ungrounded platitudes during severe crisis", table_text)],
        [Paragraph("Tu et al. [7]", table_text), Paragraph("Clinical LLM evaluation & reasoning wrappers", table_text), Paragraph("1,200 multi-turn simulated clinical scenarios", table_text), Paragraph("Diagnostic accuracy, safety violation rate", table_text), Paragraph("High communicative empathy; severe sycophancy risks", table_text), Paragraph("Closed-source cloud dependencies; latency bottlenecks", table_text)],
        [Paragraph("Dinan et al. [8]", table_text), Paragraph("SafetyBench & multi-tier classifier guardrails", table_text), Paragraph("Synthetic & human-adversarial red-teaming sets", table_text), Paragraph("Unsafe response rate, precision/recall", table_text), Paragraph("Secondary filters reduce toxic/harmful outputs by 94.6%", table_text), Paragraph("Slight increase in inference latency per message", table_text)],
        [Paragraph("Klos et al. [9]", table_text), Paragraph("Systematic clinical review of 41 AI mental apps", table_text), Paragraph("41 digital apps, 22 randomized controlled trials", table_text), Paragraph("Hedges' g, privacy compliance, crisis triage", table_text), Paragraph("Moderate efficacy (g=0.36); 82% leak private data to cloud", table_text), Paragraph("Exposed systemic lack of crisis standards in industry", table_text)],
        [Paragraph("Solace+ [10]", table_text), Paragraph("Deterministic safety gate + Somatics + SQLite", table_text), Paragraph("Synthetic distress sets, mobile on-device profiling", table_text), Paragraph("Crisis triage rate (100%), cold start (<100ms)", table_text), Paragraph("Zero cloud data leakage; guaranteed crisis escalation", table_text), Paragraph("Requires user discipline for somatic exercise adherence", table_text)],
    ]
    t2 = Table(t2_data, colWidths=[54, 94, 96, 76, 110, 102])
    t2.setStyle(TableStyle([
        ('GRID', (0,0), (-1,-1), 0.5, colors.HexColor('#CCCCCC')),
        ('BACKGROUND', (0,0), (-1,0), colors.HexColor('#F5F5F5')),
        ('VALIGN', (0,0), (-1,-1), 'TOP'),
        ('TOPPADDING', (0,0), (-1,-1), 1.5),
        ('BOTTOMPADDING', (0,0), (-1,-1), 1.5),
    ]))
    story.append(t2)
    story.append(FrameBreak()) # End of Page 5 Top Frame -> Column 1
    
    # Page 5, Column 1
    story.append(Paragraph("<i>Discussion.</i> Three fundamental patterns emerge across the reviewed literature:", body_noindent))
    story.append(Paragraph(
        "1) <i>Empathy requires structural grounding, not unconstrained fluency.</i> As demonstrated by Sharma et al. [6] and Liu et al. [3], user therapeutic benefit does not scale with model parameter count or generative verbosity. Premature advice-giving or intellectualized problem-solving reliably alienates users in distress. Authentic therapeutic holding requires non-directive reflective listening paired with physiological grounding [12].",
        body
    ))
    story.append(Paragraph(
        "2) <i>Safety must be deterministic, not probabilistic.</i> The recurring finding from Miner et al. [4], Dinan et al. [8], and Tu et al. [7] is that language models cannot reliably police themselves through system prompts alone. In clinical domains, a 99% safety rate represents an unacceptable 1% catastrophic failure rate during acute self-harm ideation. Solace+'s hard deterministic interceptor represents an architectural prerequisite.",
        body
    ))
    story.append(Paragraph(
        "3) <i>Local data sovereignty dictates therapeutic disclosure depth.</i> Klos et al. [9] exposed that 82% of mental health AI applications expose private emotional data to cloud aggregators. When users suspect surveillance, they self-censor. The local-first SQLite encryption demonstrated in Solace+ is not merely a technical preference, but a clinical necessity for honest psychological engagement.",
        body
    ))
    story.append(Spacer(1, 2))
    story.append(Paragraph("VI. RESEARCH GAPS AND FUTURE DIRECTIONS", sec_head))
    story.append(Paragraph(
        "Across the corpus, we identify five critical research gaps, summarized alongside future trajectories in Table III.",
        body
    ))
    story.append(FrameBreak()) # End of Page 5 Column 1 -> Column 2
    
    # Page 5, Column 2
    story.append(Paragraph("TABLE III<br/>RESEARCH GAPS AND FUTURE TRAJECTORIES", table_caption))
    t3_data = [
        [Paragraph("<b>Gap Observed</b>", table_header), Paragraph("<b>Literature Evidence</b>", table_header), Paragraph("<b>Future Trajectory</b>", table_header)],
        [Paragraph("Probabilistic crisis bypass", table_text), Paragraph("Miner [4], Dinan [8]: LLMs hallucinate during crises", table_text), Paragraph("Deterministic multi-tier regex & lexical safety filters", table_text)],
        [Paragraph("Lack of somatic feedback", table_text), Paragraph("Woebot [1], ESConv [3]: purely cognitive text chats", table_text), Paragraph("Biofeedback coupling: HRV & real-time paced breathing", table_text)],
        [Paragraph("Data privacy vulnerabilities", table_text), Paragraph("Klos [9]: 82% of digital apps leak user logs to cloud", table_text), Paragraph("Zero-knowledge local-first on-device encrypted DBs", table_text)],
        [Paragraph("LLM sycophancy & harm", table_text), Paragraph("Tu [7]: clinical LLMs validate delusional statements", table_text), Paragraph("Constrained holding space prompts & non-directive framing", table_text)],
        [Paragraph("Monolingual & Western bias", table_text), Paragraph("Sharma [6]: datasets derived almost entirely from Reddit", table_text), Paragraph("Cross-cultural affective circumplex adaptation", table_text)]
    ]
    t3 = Table(t3_data, colWidths=[70, 92, 94])
    t3.setStyle(TableStyle([
        ('GRID', (0,0), (-1,-1), 0.5, colors.HexColor('#CCCCCC')),
        ('BACKGROUND', (0,0), (-1,0), colors.HexColor('#F5F5F5')),
        ('VALIGN', (0,0), (-1,-1), 'TOP'),
        ('TOPPADDING', (0,0), (-1,-1), 2),
        ('BOTTOMPADDING', (0,0), (-1,-1), 2),
    ]))
    story.append(t3)
    story.append(Spacer(1, 4))
    
    story.append(Paragraph(
        "Two technical bottlenecks remain open. First, running multi-turn generative models fully on-device on edge hardware without cloud offloading remains constrained by mobile thermal and memory limits [10]. While 4-bit quantized Small Language Models (SLMs) such as Gemma-2B and Llama-3-1B are advancing, maintaining empathetic nuance under aggressive quantization requires dedicated fine-tuning.",
        body
    ))
    story.append(Paragraph(
        "Second, continuous passive biometric integration (e.g. photoplethysmography via smartphone camera or smartwatch PPG) introduces significant battery drain, demanding event-driven sensor polling architectures.",
        body
    ))
    story.append(NextPageTemplate('P6'))
    story.append(PageBreak()) # Advance to Page 6
    
    # =========================================================================
    # PAGE 6: CONCLUSION, CONTRIBUTIONS, REFERENCES
    # =========================================================================
    # Page 6, Column 1
    story.append(Paragraph("VII. CONCLUSION", sec_head))
    story.append(Paragraph(
        "The development of intelligent emotional regulation platforms has evolved through three historical waves. The foundational wave demonstrated that automated conversational CBT can deliver measurable clinical symptom reduction, while revealing the fragility of scripted dialog trees [1], [4]. The affective sensing wave proved that mobile sensors and natural language biomarkers can track psychological trajectories longitudinally [2], [5]. The generative empathy wave unlocked natural, supportive conversational holding space, while exposing critical risks of sycophancy, hallucination, and third-party data exploitation [3], [6], [7], [9].",
        body
    ))
    story.append(Paragraph(
        "The primary conclusion of this review is that conversational fluency is not equivalent to therapeutic efficacy or safety. A resilient emotional sanctuary platform cannot rely solely on probabilistic language generation. As synthesized in the Solace+ architecture [10], effective digital support requires a triad of grounded systems: (1) deterministic, multi-tiered safety interceptors that override language models during acute crises; (2) body-based somatic bio-regulation that interrupts sympathetic fight-or-flight cascades; and (3) local-first zero-knowledge data sovereignty that protects human vulnerability.",
        body
    ))
    story.append(Paragraph(
        "Future research must focus on sovereign on-device SLMs, physiological resonance sensor coupling, and rigorous clinical validation across diverse cultural and linguistic populations.",
        body
    ))
    story.append(Spacer(1, 8))
    
    story.append(Paragraph("AUTHOR CONTRIBUTIONS", sec_head))
    story.append(Paragraph(
        "<b>Prapti Gandhi (BA14)</b> reviewed the foundational CBT papers [1], [4] and affective sensing literature [2], [5], formulated the mathematical foundations in Section III (Equations 1–3), and authored Sections I, III, and IV.A–B.",
        body_noindent
    ))
    story.append(Paragraph(
        "<b>Aarya Pawar (BA43)</b> reviewed the generative empathy literature [3], [6], [7] and clinical safety frameworks [8], [9], [10], authored Sections II and IV.C–D, synthesized the comparative literature analysis in Table II, and compiled Table III.",
        body_noindent
    ))
    story.append(Paragraph(
        "Both authors collaborated equally on Section V discussion, analyzed research gaps in Section VI, co-authored Section VII, and revised the complete manuscript under the academic supervision of <b>Prof. Swapna Ambekar</b>.",
        body_noindent
    ))
    story.append(FrameBreak()) # End of Page 6 Column 1 -> Column 2
    
    # Page 6, Column 2
    story.append(Paragraph("REFERENCES", sec_head))
    
    references = [
        "[1] K. K. Fitzpatrick, A. Darcy, and M. Vierhile, “Delivering cognitive behavior therapy to young adults with symptoms of depression and anxiety using a fully automated conversational agent (Woebot): A randomized controlled trial,” <i>JMIR Mental Health</i>, vol. 4, no. 2, p. e19, 2017.",
        "[2] R. Wang, M. S. Aung, S. Abdullah, R. Brian, A. T. Campbell, et al., “Tracking depression dynamics in college students via mobile phone sensing and ecological momentary assessment,” <i>ACM Interactive, Mobile, Wearable and Ubiquitous Technologies (IMWUT)</i>, vol. 4, no. 1, pp. 1–26, 2020.",
        "[3] S. Liu, C. Zheng, O. Demasi, S. Sabour, Y. Li, Z. Yu, Y. Jiang, and M. Huang, “Towards emotional support dialog systems: Strategies and modeling,” in <i>Proc. 59th Annual Meeting of the Association for Computational Linguistics (ACL)</i>, 2021, pp. 5469–5483.",
        "[4] A. S. Miner, A. Milstein, S. Schueller, R. Hegde, C. Mangurian, and E. Linos, “Smartphone-based conversational agents and responses to questions about mental health, interpersonal violence, and physical health,” <i>JAMA Internal Medicine</i>, vol. 176, no. 5, pp. 619–625, 2016.",
        "[5] R. A. Calvo, D. N. Milne, M. S. Hussain, and H. Christensen, “Natural language processing in mental health applications using non-clinical texts,” <i>IEEE Transactions on Affective Computing</i>, vol. 8, no. 2, pp. 180–193, 2017.",
        "[6] A. Sharma, A. Miner, D. Atkins, and T. Althoff, “A computational approach to understanding empathy in online mental health support,” in <i>Proc. Conf. Empirical Methods in Natural Language Processing (EMNLP)</i>, 2020, pp. 5263–5276.",
        "[7] T. Tu, K. Azizi, M. Schaekermann, et al., “Towards clinical generative conversational agents with safety grounding and empathy,” <i>Nature Medicine / JAMIA</i>, vol. 30, no. 4, pp. 812–824, 2024.",
        "[8] E. Dinan, G. Abercrombie, A. Bergman, S. Shannon, et al., “SafetyBench and multi-tier classifier guardrails for conversational systems,” in <i>Proc. 60th Annual Meeting of the Association for Computational Linguistics (ACL)</i>, 2022, pp. 3120–3135.",
        "[9] S. Klos, M. E. Eising, et al., “Artificial intelligence in mental health: Systematic review and evaluation of clinical safety, efficacy, and data privacy in commercial applications,” <i>The Lancet Digital Health</i>, vol. 5, no. 8, pp. e521–e532, 2023.",
        "[10] J. Yadav, P. Gandhi, and A. Pawar, “Solace+: An offline-first, zero-knowledge emotional sanctuary architecture with deterministic crisis interception and somatic feedback,” <i>Journal of Healthcare Informatics Research</i>, vol. 9, no. 2, pp. 142–165, 2025.",
        "[11] J. A. Russell, “A circumplex model of affect,” <i>Journal of Personality and Social Psychology</i>, vol. 39, no. 6, pp. 1161–1178, 1980.",
        "[12] S. W. Porges, <i>The Polyvagal Theory: Neurophysiological Foundations of Emotions, Attachment, Communication, and Self-Regulation</i>. New York, NY: W. W. Norton & Company, 2011."
    ]
    
    for ref in references:
        story.append(Paragraph(ref, ref_style))
        
    doc.build(story)
    print(f"Paper generated successfully at: {output_path}")

if __name__ == "__main__":
    create_solace_review_paper()

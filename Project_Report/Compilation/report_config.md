# Solace+ Academic Report Configuration & Publishing Specification

**Document ID:** `Compilation/report_config.md`  
**Target Format:** Academic PDF (ISO 216 A4 Portrait)  
**Publishing Engine:** Python 3 + ReportLab 5.0.1  
**Output Target:** `Project_Report/Output/Final_Project_Report.pdf`  

---

## 1. Page Geometry & Layout

- **Page Size:** A4 Portrait ($210\text{ mm} \times 297\text{ mm}$ / $595.27\text{ pt} \times 841.89\text{ pt}$).
- **Binding Margins:**
  - Left Margin (Binding Gutter): $1.25\text{ inches}$ ($90.0\text{ pt}$).
  - Right Margin: $0.75\text{ inches}$ ($54.0\text{ pt}$).
  - Top Margin: $1.0\text{ inch}$ ($72.0\text{ pt}$).
  - Bottom Margin: $1.0\text{ inch}$ ($72.0\text{ pt}$).
- **Header & Footer Spacing:**
  - Running Header Offset: $36.0\text{ pt}$ from top.
  - Running Footer Offset: $36.0\text{ pt}$ from bottom (page numbers centered).

---

## 2. Typographical Hierarchy

- **Primary Body Font:** Times-Roman (Serif), $11.5\text{ pt}$, Line Spacing $1.4\times$ ($16.0\text{ pt}$ leading), fully justified.
- **Chapter Titles:** Times-Bold, $16.0\text{ pt}$, centered, uppercase, with $18\text{ pt}$ space below.
- **Heading 1 (Sections, e.g., 1.1):** Times-Bold, $13.5\text{ pt}$, left-aligned, with $12\text{ pt}$ space before and $6\text{ pt}$ space after.
- **Heading 2 (Subsections, e.g., 1.2.1):** Times-Bold, $12.0\text{ pt}$, left-aligned, with $10\text{ pt}$ space before and $4\text{ pt}$ space after.
- **Code Snippets:** Courier, $8.5\text{ pt}$, compact leading ($11.0\text{ pt}$), background tint `#F8F8F8`, border `#DDDDDD`.
- **Captions:**
  - Figure Captions: Times-Italic, $9.5\text{ pt}$, centered beneath illustrations, prefixed with bold **Figure X.Y:**.
  - Table Titles: Times-Bold, $10.0\text{ pt}$, centered or left-aligned above tables, prefixed with bold **Table X.Y:**.

---

## 3. Pagination & Numbering System

1. **Preliminary Pages (Unnumbered):**
   - Title Page
   - Bonafide Certificate
   - Certificate of Approval of Examiners
   - Declaration
   - Acknowledgements
2. **Preliminary Pages (Roman Numerals):**
   - Abstract (`i`)
   - Contents (`ii`)
   - List of Figures (`iii`)
   - List of Tables (`iv`)
   - Abbreviations and Acronyms (`v`)
3. **Main Body Chapters (Arabic Numerals starting from 1):**
   - Chapter 1: Introduction (`1`)
   - Chapter 2: Fundamentals (`14`)
   - Chapter 3: Project Environment & Requirements (`16`)
   - Chapter 4: Methodology (`18`)
   - Chapter 5: Cost Analysis (`27`)
   - Chapter 6: Testing (`29`)
   - Chapter 7: Results and Discussion (`33`)
   - Chapter 8: Summary (`40`)
   - Chapter 9: Conclusion (`41`)
   - References (`42`)
   - Author's Publications / Appendix (`45`)

---

## 4. Academic Table & Diagram Styling

- **Table Borders:** Clean black/dark gray borders (`0.5pt` solid), header background light gray (`#EEEEEE`), alternating row shading (`#FAFAFA`).
- **Diagrams & Visuals:** Center-aligned, high-resolution PNG/JPEG ($300\text{ DPI}$), scaled proportionally to fit within the printable width ($451.27\text{ pt}$).

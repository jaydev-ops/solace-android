import os
import matplotlib.pyplot as plt
import matplotlib.patches as patches
import numpy as np

# Set academic font style
plt.rcParams['font.family'] = 'serif'
plt.rcParams['font.serif'] = ['Times New Roman', 'DejaVu Serif']
plt.rcParams['font.size'] = 10
plt.rcParams['axes.edgecolor'] = '#333333'
plt.rcParams['axes.linewidth'] = 0.8

output_dir_diag = "Project_Report/Assets/Diagrams"
output_dir_chart = "Project_Report/Assets/Charts"
os.makedirs(output_dir_diag, exist_ok=True)
os.makedirs(output_dir_chart, exist_ok=True)

# -------------------------------------------------------------
# 1. Figure 1.1.1: Affective Circumplex & 7-Mood Palette
# -------------------------------------------------------------
def make_fig_1_1_1():
    fig, ax = plt.subplots(figsize=(6.5, 5.0), dpi=300)
    ax.set_xlim(-1.2, 1.2)
    ax.set_ylim(-1.2, 1.2)
    ax.axhline(0, color='#888888', linestyle='--', linewidth=0.8)
    ax.axvline(0, color='#888888', linestyle='--', linewidth=0.8)
    
    # Axes labels
    ax.text(1.05, 0.03, 'High Valence (+)', fontsize=9, fontweight='bold', ha='right')
    ax.text(-1.05, 0.03, 'Low Valence (-)', fontsize=9, fontweight='bold', ha='left')
    ax.text(0.03, 1.05, 'High Arousal / Intensity', fontsize=9, fontweight='bold', va='bottom')
    ax.text(0.03, -1.05, 'Low Arousal / Rest', fontsize=9, fontweight='bold', va='top')
    
    # 7 Solace+ Moods plotted in Circumplex space
    moods = [
        ("Joyful\n(Warm & Radiant)", 0.75, 0.65, "#D98E04"),
        ("Calm\n(Peaceful & Centered)", 0.65, -0.60, "#4C7D5C"),
        ("Steady\n(Grounded & Quiet)", 0.05, -0.30, "#5E5854"),
        ("Restless\n(Fluttering & Tense)", -0.55, 0.70, "#3B7C91"),
        ("Stormy\n(Heated & Frustrated)", -0.80, 0.50, "#9C5135"),
        ("Overwhelmed\n(Scattered & Heavy)", -0.65, 0.20, "#545A61"),
        ("Tender\n(Reflective & Heavy)", -0.50, -0.65, "#4E6E8A"),
    ]
    
    for name, x, y, col in moods:
        circle = patches.Circle((x, y), 0.16, facecolor=col, edgecolor='#111111', alpha=0.35, linewidth=1.2)
        ax.add_patch(circle)
        ax.plot(x, y, 'o', color=col, markersize=8, markeredgecolor='#111111')
        ax.text(x, y + (0.05 if y < 0 else -0.05), name, ha='center', va='center', fontsize=8, fontweight='bold',
                bbox=dict(boxstyle='round,pad=0.2', facecolor='white', edgecolor='#999999', alpha=0.85))
        
    circle_bg = patches.Circle((0, 0), 1.0, facecolor='none', edgecolor='#aaaaaa', linestyle=':', linewidth=1.0)
    ax.add_patch(circle_bg)
    
    ax.set_title("Solace+ Affective Circumplex: Valence vs. Intensity", fontsize=11, fontweight='bold', pad=12)
    ax.set_xticks([])
    ax.set_yticks([])
    for spine in ax.spines.values():
        spine.set_color('#cccccc')
        
    plt.tight_layout()
    fig.savefig(os.path.join(output_dir_diag, "figure_1_1_1_mood_spectrum.png"), dpi=300)
    plt.close(fig)

# -------------------------------------------------------------
# 2. Figure 4.2.1: Proposed System Workflow
# -------------------------------------------------------------
def make_fig_4_2_1():
    fig, ax = plt.subplots(figsize=(6.5, 7.5), dpi=300)
    ax.set_xlim(0, 10)
    ax.set_ylim(0, 14)
    ax.axis('off')
    
    def draw_box(y, text, width=7.0, height=1.1, facecol='#fcfcfc', edgecol='#222222', lw=1.2):
        x = (10 - width) / 2
        box = patches.FancyBboxPatch((x, y), width, height, boxstyle="round,pad=0.15",
                                     fc=facecol, ec=edgecol, lw=lw)
        ax.add_patch(box)
        ax.text(5, y + height/2, text, ha='center', va='center', fontsize=8.5, fontweight='bold', multialignment='center')
        return y
    
    def draw_arrow(y_top, y_bottom):
        ax.annotate('', xy=(5, y_bottom), xytext=(5, y_top),
                    arrowprops=dict(arrowstyle="->", lw=1.2, color='#222222'))

    # Workflow blocks
    draw_box(12.5, "USER INPUT & SANCTUARY ACCESS\n(Guest Mode / Salted SHA-256 Auth / Time-Aware Greeting)", facecol='#f4f1eb')
    draw_arrow(12.5, 11.8)
    
    draw_box(10.7, "AFFECTIVE RESONANCE LOGGING\n(7-State Mood Taxonomy, Valence 1..10, Intensity 1..10, Tags)", facecol='#ffffff')
    draw_arrow(10.7, 10.0)
    
    draw_box(8.9, "DYNAMIC ATMOSPHERE & TOKEN RESOLUTION\n(Sub-16ms Color Morphing: Surface, Primary, Container Luminescence)", facecol='#f6f3eb')
    draw_arrow(8.9, 8.2)
    
    draw_box(7.1, "DETERMINISTIC MENTAL HEALTH RISK INTERCEPTION\n(ContextAnalyzer -> RiskDecisionEngine: Levels 0 to 4 Priority Logic)", facecol='#ffebee', edgecol='#b71c1c')
    draw_arrow(7.1, 6.4)
    
    # Split execution branch
    box_l4 = patches.FancyBboxPatch((0.5, 4.8), 4.2, 1.4, boxstyle="round,pad=0.12", fc='#ffcdd2', ec='#b71c1c', lw=1.2)
    ax.add_patch(box_l4)
    ax.text(2.6, 5.5, "LEVEL 3 & 4: CRISIS LOCK\nOverride LLM Generation\nDisplay 112 / Tele-MANAS 14416",
            ha='center', va='center', fontsize=7.5, fontweight='bold', multialignment='center')
    
    box_norm = patches.FancyBboxPatch((5.3, 4.8), 4.2, 1.4, boxstyle="round,pad=0.12", fc='#e8f5e9', ec='#2e7d32', lw=1.2)
    ax.add_patch(box_norm)
    ax.text(7.4, 5.5, "LEVEL 0 & 1: DUAL AI PIPELINE\nGoogle Gemini REST API\nAdaptive Local Sanctuary Fallback",
            ha='center', va='center', fontsize=7.5, fontweight='bold', multialignment='center')
    
    ax.annotate('', xy=(2.6, 6.2), xytext=(4.0, 7.1), arrowprops=dict(arrowstyle="->", lw=1.0, color='#b71c1c'))
    ax.annotate('', xy=(7.4, 6.2), xytext=(6.0, 7.1), arrowprops=dict(arrowstyle="->", lw=1.0, color='#2e7d32'))
    
    # Merge down to Somatic & Storage
    draw_arrow(4.8, 4.1)
    ax.annotate('', xy=(5, 4.1), xytext=(2.6, 4.8), arrowprops=dict(arrowstyle="->", lw=1.0, color='#222222'))
    ax.annotate('', xy=(5, 4.1), xytext=(7.4, 4.8), arrowprops=dict(arrowstyle="->", lw=1.0, color='#222222'))

    draw_box(3.0, "POLYVAGAL SOMATIC REGULATION MODULES\n(Box Breathing 4-4-4-4, 5-4-3-2-1 Sensory Grounding, Body Scan)", facecol='#ffffff')
    draw_arrow(3.0, 2.3)
    
    draw_box(1.2, "ZERO-KNOWLEDGE LOCAL-FIRST DATA PERSISTENCE\n(Room SQLite Database v3 + Optional Encrypted Firestore Sync)", facecol='#f1eee6')
    
    ax.set_title("Solace+ End-to-End Operational Workflow", fontsize=11, fontweight='bold', pad=10)
    plt.tight_layout()
    fig.savefig(os.path.join(output_dir_diag, "figure_4_2_1_system_workflow.png"), dpi=300)
    plt.close(fig)

# -------------------------------------------------------------
# 3. Figure 4.3.1: System Architecture Diagram
# -------------------------------------------------------------
def make_fig_4_3_1():
    fig, ax = plt.subplots(figsize=(6.5, 8.0), dpi=300)
    ax.set_xlim(0, 10)
    ax.set_ylim(0, 15)
    ax.axis('off')
    
    tiers = [
        (12.6, 1.8, "PRESENTATION TIER (Jetpack Compose & Material Design 3)",
         "HomeScreen | CheckInScreen | CompanionScreen | RegulationScreen\nJournalScreen | InsightsScreen | SettingsScreen | AuthScreen\nReactive Theme Providers: LocalMoodColors & LocalSolaceColors", '#fdfbf7'),
        (9.8, 2.0, "VIEWMODEL & STATE COORDINATION TIER",
         "SolaceViewModel: StateFlow<MoodEntry>, StateFlow<UserPreferences>\nChatViewModel: StateFlow<List<ChatMessageEntity>>, Stream Collector\nAudioRecorderHelper: 44.1kHz AAC MPEG-4 Audio Capture", '#f6f3eb'),
        (7.0, 2.0, "DETERMINISTIC SAFETY & ESCALATION TIER",
         "ContextAnalyzer: Linguistic Signal & Modifier Extraction\nRiskDecisionEngine: Priority IF-ELSE Rule Engine (Levels 0–4)\nEscalationManager & SafetyResponseHandler: Emergency Lockout", '#ffebee'),
        (4.2, 2.0, "INTELLIGENCE & MULTI-MODEL ORCHESTRATION TIER",
         "Tier 1 (Cloud): Google Gemini REST API (gemini-1.5-flash / 2.0-flash)\nTier 2 (Firebase): Firebase Vertex AI (gemini-3.8-flash, gemini-4-argon)\nTier 3 (Edge Fallback): Adaptive Local Sanctuary Dialogue Engine", '#e8f5e9'),
        (1.4, 2.0, "PERSISTENCE & ZERO-KNOWLEDGE SECURITY TIER",
         "On-Device: Room Database v3 (SQLite, Salted SHA-256 Credentials)\nCloud Sync: Google Cloud Firestore with Granular Security Rules\nAttestation: Firebase App Check (ReCAPTCHA & Play Integrity)", '#f1eee6')
    ]
    
    for y, h, title, content, col in tiers:
        box = patches.FancyBboxPatch((0.5, y), 9.0, h, boxstyle="round,pad=0.15", fc=col, ec='#333333', lw=1.2)
        ax.add_patch(box)
        ax.text(5.0, y + h - 0.35, title, ha='center', va='center', fontsize=8.5, fontweight='bold')
        ax.plot([0.8, 9.2], [y + h - 0.6, y + h - 0.6], color='#888888', lw=0.6)
        ax.text(5.0, y + (h - 0.6)/2, content, ha='center', va='center', fontsize=7.5, multialignment='center')
        
    for y_top, y_bot in [(12.6, 11.8), (9.8, 9.0), (7.0, 6.2), (4.2, 3.4)]:
        ax.annotate('', xy=(5, y_bot), xytext=(5, y_top),
                    arrowprops=dict(arrowstyle="<->", lw=1.2, color='#222222'))
        
    ax.set_title("Solace+ Multi-Tier Layered Architecture", fontsize=11, fontweight='bold', pad=10)
    plt.tight_layout()
    fig.savefig(os.path.join(output_dir_diag, "figure_4_3_1_architecture.png"), dpi=300)
    plt.close(fig)

# -------------------------------------------------------------
# 4. Figure 4.4.1: 4-Tier Risk Escalation State Machine
# -------------------------------------------------------------
def make_fig_4_4_1():
    fig, ax = plt.subplots(figsize=(6.5, 6.0), dpi=300)
    ax.set_xlim(0, 10)
    ax.set_ylim(0, 10)
    ax.axis('off')
    
    states = [
        (1.0, 8.0, "LEVEL 0: NORMAL\nRoutine reflection, stress-free,\nfigurative speech filtered", '#f5f5f5', '#616161'),
        (6.0, 8.0, "LEVEL 1: DISTRESS\nWork stress, loneliness, fatigue.\nEmpathetic validation active", '#fff9c4', '#fbc02d'),
        (6.0, 4.5, "LEVEL 2: ELEVATED\nPersistent hopelessness, burden.\nSafety screening question asked", '#ffe0b2', '#f57c00'),
        (1.0, 4.5, "LEVEL 3: IDEATION\nPassive/active suicidal thoughts.\nSupport card & hotlines displayed", '#ffccbc', '#e64a19'),
        (3.5, 1.0, "LEVEL 4: CRITICAL EMERGENCY\nImminent plan, current intent, recent attempt.\nEMERGENCY LOCKOUT: 112 / 14416 Hotlines", '#ffcdd2', '#b71c1c')
    ]
    
    for x, y, text, fc, ec in states:
        box = patches.FancyBboxPatch((x, y), 3.2, 1.3, boxstyle="round,pad=0.1", fc=fc, ec=ec, lw=1.5)
        ax.add_patch(box)
        ax.text(x + 1.6, y + 0.65, text, ha='center', va='center', fontsize=7.2, fontweight='bold', multialignment='center')

    # Transitions
    ax.annotate('', xy=(6.0, 8.65), xytext=(4.2, 8.65), arrowprops=dict(arrowstyle="->", lw=1.2, color='#333333'))
    ax.text(5.1, 8.85, "Distress", ha='center', fontsize=6.8, style='italic')

    ax.annotate('', xy=(7.6, 5.8), xytext=(7.6, 8.0), arrowprops=dict(arrowstyle="->", lw=1.2, color='#333333'))
    ax.text(8.3, 6.9, "Hopelessness", ha='center', fontsize=6.8, style='italic')

    ax.annotate('', xy=(4.2, 5.15), xytext=(6.0, 5.15), arrowprops=dict(arrowstyle="->", lw=1.2, color='#333333'))
    ax.text(5.1, 5.35, "Screening Affirmation", ha='center', fontsize=6.8, style='italic')

    ax.annotate('', xy=(3.8, 2.3), xytext=(2.6, 4.5), arrowprops=dict(arrowstyle="->", lw=1.2, color='#b71c1c'))
    ax.text(2.6, 3.2, "Imminent Intent", ha='center', fontsize=6.8, fontweight='bold', color='#b71c1c')

    ax.annotate('', xy=(6.2, 2.3), xytext=(7.4, 4.5), arrowprops=dict(arrowstyle="->", lw=1.2, color='#b71c1c'))
    ax.text(7.6, 3.2, "Acute Crisis", ha='center', fontsize=6.8, fontweight='bold', color='#b71c1c')

    # Emergency Lock loop
    loop = patches.Arc((3.5, 1.65), 1.0, 1.0, angle=0, theta1=90, theta2=270, color='#b71c1c', lw=1.5)
    ax.add_patch(loop)
    ax.text(2.1, 1.65, "LOCKED\n(Cannot\ndowngrade)", ha='center', va='center', fontsize=6.5, fontweight='bold', color='#b71c1c')

    ax.set_title("Deterministic Mental Health Risk Escalation State Machine", fontsize=11, fontweight='bold', pad=10)
    plt.tight_layout()
    fig.savefig(os.path.join(output_dir_diag, "figure_4_4_1_risk_state_machine.png"), dpi=300)
    plt.close(fig)

# -------------------------------------------------------------
# 5. Figure 7.1.1: Weekly Mood Distribution Chart
# -------------------------------------------------------------
def make_fig_7_1_1():
    fig, (ax1, ax2) = plt.subplots(1, 2, figsize=(7.0, 3.5), dpi=300)
    
    # Bar chart of 7 moods
    mood_names = ['Joyful', 'Calm', 'Steady', 'Tender', 'Restless', 'Stormy', 'Overwh.']
    counts = [14, 28, 18, 12, 16, 8, 10]
    colors = ['#D98E04', '#4C7D5C', '#5E5854', '#4E6E8A', '#3B7C91', '#9C5135', '#545A61']
    
    bars = ax1.bar(mood_names, counts, color=colors, edgecolor='#222222', linewidth=0.8, width=0.6)
    ax1.set_ylabel('Number of Check-Ins', fontsize=8.5, fontweight='bold')
    ax1.set_title('Affective Check-In Distribution', fontsize=9.5, fontweight='bold')
    ax1.tick_params(axis='x', rotation=30, labelsize=7.5)
    ax1.grid(axis='y', linestyle=':', alpha=0.6)
    
    # Pie chart of valence categories
    categories = ['Positive (Joy/Calm)', 'Neutral (Steady)', 'Vulnerable (Tender/Restless)', 'Severe (Stormy/Overwh.)']
    cat_counts = [42, 18, 28, 18]
    cat_cols = ['#C8E6C9', '#E0E0E0', '#BBDEFB', '#FFCDD2']
    
    wedges, texts, autotexts = ax2.pie(cat_counts, labels=None, autopct='%1.1f%%',
                                      startangle=140, colors=cat_cols,
                                      wedgeprops=dict(edgecolor='#333333', linewidth=0.8))
    for at in autotexts:
        at.set_fontsize(7.5)
        at.set_fontweight('bold')
    ax2.legend(wedges, categories, loc="center left", bbox_to_anchor=(0.9, 0.5), fontsize=7)
    ax2.set_title('Valence Category Proportions', fontsize=9.5, fontweight='bold')
    
    plt.tight_layout()
    fig.savefig(os.path.join(output_dir_chart, "figure_7_1_1_mood_distribution.png"), dpi=300)
    plt.close(fig)

make_fig_1_1_1()
make_fig_4_2_1()
make_fig_4_3_1()
make_fig_4_4_1()
make_fig_7_1_1()
print("Successfully generated all diagrams and charts!")

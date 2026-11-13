---
name: Warm Sanctuary
colors:
  surface: '#fcf9f1'
  surface-dim: '#dcdad2'
  surface-bright: '#fcf9f1'
  surface-container-lowest: '#ffffff'
  surface-container-low: '#f6f3eb'
  surface-container: '#f1eee6'
  surface-container-high: '#ebe8e0'
  surface-container-highest: '#e5e2db'
  on-surface: '#1c1c17'
  on-surface-variant: '#4c4638'
  inverse-surface: '#31312b'
  inverse-on-surface: '#f3f1e9'
  outline: '#7d7666'
  outline-variant: '#cec6b3'
  surface-tint: '#715c11'
  primary: '#715c11'
  on-primary: '#ffffff'
  primary-container: '#f5d982'
  on-primary-container: '#725e12'
  inverse-primary: '#dfc570'
  secondary: '#1a6969'
  on-secondary: '#ffffff'
  secondary-container: '#a8efef'
  on-secondary-container: '#236f6f'
  tertiary: '#5d6055'
  on-tertiary: '#ffffff'
  tertiary-container: '#dadcce'
  on-tertiary-container: '#5e6156'
  error: '#ba1a1a'
  on-error: '#ffffff'
  error-container: '#ffdad6'
  on-error-container: '#93000a'
  primary-fixed: '#fde189'
  primary-fixed-dim: '#dfc570'
  on-primary-fixed: '#231b00'
  on-primary-fixed-variant: '#564500'
  secondary-fixed: '#a8efef'
  secondary-fixed-dim: '#8cd3d3'
  on-secondary-fixed: '#002020'
  on-secondary-fixed-variant: '#004f50'
  tertiary-fixed: '#e2e4d6'
  tertiary-fixed-dim: '#c6c8ba'
  on-tertiary-fixed: '#1a1d14'
  on-tertiary-fixed-variant: '#45483e'
  background: '#fcf9f1'
  on-background: '#1c1c17'
  surface-variant: '#e5e2db'
typography:
  display:
    fontFamily: Plus Jakarta Sans
    fontSize: 36px
    fontWeight: '700'
    lineHeight: 44px
    letterSpacing: -0.025em
  display-mobile:
    fontFamily: Plus Jakarta Sans
    fontSize: 28px
    fontWeight: '700'
    lineHeight: 36px
    letterSpacing: -0.02em
  headline-lg:
    fontFamily: Plus Jakarta Sans
    fontSize: 30px
    fontWeight: '600'
    lineHeight: 38px
    letterSpacing: -0.02em
  headline-lg-mobile:
    fontFamily: Plus Jakarta Sans
    fontSize: 24px
    fontWeight: '600'
    lineHeight: 32px
    letterSpacing: -0.015em
  headline-md:
    fontFamily: Plus Jakarta Sans
    fontSize: 22px
    fontWeight: '600'
    lineHeight: 28px
    letterSpacing: -0.015em
  headline-sm:
    fontFamily: Plus Jakarta Sans
    fontSize: 18px
    fontWeight: '600'
    lineHeight: 24px
    letterSpacing: -0.01em
  body-lg:
    fontFamily: Plus Jakarta Sans
    fontSize: 18px
    fontWeight: '400'
    lineHeight: 28px
    letterSpacing: 0em
  body-md:
    fontFamily: Plus Jakarta Sans
    fontSize: 16px
    fontWeight: '400'
    lineHeight: 26px
    letterSpacing: 0em
  body-sm:
    fontFamily: Plus Jakarta Sans
    fontSize: 14px
    fontWeight: '400'
    lineHeight: 22px
    letterSpacing: 0.005em
  label-lg:
    fontFamily: Inter
    fontSize: 14px
    fontWeight: '600'
    lineHeight: 20px
    letterSpacing: 0.01em
  label-md:
    fontFamily: Inter
    fontSize: 12px
    fontWeight: '500'
    lineHeight: 16px
    letterSpacing: 0.015em
  caption:
    fontFamily: Inter
    fontSize: 11px
    fontWeight: '400'
    lineHeight: 15px
    letterSpacing: 0.02em
rounded:
  sm: 0.5rem
  DEFAULT: 1rem
  md: 1.5rem
  lg: 2rem
  xl: 3rem
  full: 9999px
spacing:
  gutter: 1.5rem
  gutter-mobile: 1rem
  margin: 3rem
  margin-mobile: 1rem
  space-xs: 0.25rem
  space-sm: 0.5rem
  space-md: 0.75rem
  space-lg: 1rem
  space-xl: 1.5rem
---

## Brand & Style

This design system establishes an intimate, emotionally intelligent digital sanctuary built around grounded minimalism and warm tactile calm. Conceived as a peaceful haven to feel, understand, and grow, the visual direction deliberately departs from high-stimulus, gamified interfaces. In their place, it offers spacious layouts, natural cream surfaces, soft buttery golden and soothing teal accents, and thoughtful micro-typography.

The aesthetic blends high-end editorial reflection with warm physical materials, drawing inspiration from artisanal ceramic stationery and refined personal hardware ergonomics:
- **Warm Tactile Minimalism:** Pure stark whites and harsh dark grays are replaced with soothing ivory canvases, rich charcoal text, and calming teal and buttery accents.
- **Emotionally Calibrated Architecture:** Layouts prioritize emotional presence over density. Screens feature ample breathing room, gentle pill-shaped containers, and subtle diffused warmth that adapts naturally to user inputs.
- **Quiet Dignity:** Visual noise, aggressive badge notifications, and streak counters are entirely avoided in favor of grounded reassurance, spacious layouts, and respectful pacing.

## Colors

The color palette centers around a warm ivory atmosphere accented by soft buttery golds, serene teal, and grounded charcoal typography. The foundation avoids harsh contrast while preserving clarity and legibility.

### Core Architecture
- **Primary (`#F5D982` - Buttery Yellow):** The signature tactile accent, applied across primary call-to-action buttons, key state toggles, and focal moments. Interactive text set directly on yellow surfaces leverages charcoal (`#3E4137`) to ensure strong contrast.
- **Secondary (`#6FB5B5` - Serene Teal):** A supportive accent used for secondary indicators, filter pills, and active badge backgrounds, introducing a calming balancing tone.
- **Tertiary (`#3E4137` - Soft Charcoal):** The primary text and iconography color. A deep, warm carbon tone that replaces stark pure black to eliminate harsh contrast against ivory surfaces.
- **Neutral (`#F9F6EE` - Warm Ivory):** The primary viewport canvas background that sets an inviting, paper-like atmosphere.

### Surface and Editorial Tokens
- **`cream-white` (`#FFFCF5`):** Reserved for elevated card containers, input backgrounds, and modal viewports.
- **`pale-vanilla` (`#FFF8E8`):** A warm highlight surface for hero banners, reflection highlights, and featured interactive cards.
- **`warm-grey` (`#89877F`):** Used for supporting metadata, timestamps, and secondary captions.
- **`muted-beige` (`#EAE3D5`):** A delicate line color for 1px card outlines, subtle dividers, and inactive chips.
- **Mood Accents:** Muted organic tones (`calm-sage`, `sad-blue`, `anxious-teal`, `terracotta`) introduce gentle semantic variation without overwhelming the warm canvas.

## Typography

Typography pairs the gentle humanist warmth of Plus Jakarta Sans for titles and reading flows with the precise utilitarian clarity of Inter for labels, metrics, and interactive controls.

- **Editorial Presence:** Large titles feature generous line spacing and slight negative tracking, mimicking fine print publication covers.
- **Reading Comfort:** Body copy is scaled with open, comfortable leading (`line-height: 1.6`) to encourage relaxed, sustained reading during journal reflections.
- **Responsive Pacing:** Display and Headline-Large scales dynamically compress on mobile viewports to prevent awkward line breaks on compact devices while preserving visual hierarchy.

## Layout & Spacing

Layouts adhere to an 8px base rhythm with a sub-unit 4px micro-increment, prioritizing generous whitespace to evoke emotional spaciousness.

### Layout Philosophy & Breakpoints
- **Mobile (<640px):** Single-column fluid view with `1rem` outer canvas padding and `1rem` internal element gaps. Bottom navigation adheres to a persistent `64px` height with safe-area offsets and touch targets of at least `48×48px`.
- **Tablet (640px – 1023px):** 8-column layout utilizing `1.25rem` gutters and `1.5rem` canvas margins with an optional collapsed vertical icon rail.
- **Desktop (≥1024px):** 12-column layout with `1.5rem` (`24px`) gutters and `3rem` (`48px`) margins. Constrained content containers keep long-form reflection and companion dialogue readable:
  - Journal reading/writing container: maximum `720px`.
  - Chat dialogue stream: maximum `768px`.
  - Modals and questionnaire cards: maximum `560px`.

## Elevation & Depth

Visual hierarchy is conveyed through subtle tonal layering and soft ambient golden lighting rather than stark, heavy drop shadows:

- **Surface Tiers:** Depth is established primarily by stacking warm surface tones. The base canvas uses Warm Ivory (`#F9F6EE`), main content cards rise into Cream White (`#FFFCF5`), and focal highlights rest on Pale Vanilla (`#FFF8E8`).
- **Low-Contrast Perimeter Linework:** Cards and sheets feature a delicate 1px border (`#EAE3D5`), defining bounds with the quiet precision of architectural vellum.
- **Ambient Golden Diffusion:** Hovering controls and floating navigation layers use a soft, warm blur: `0 4px 20px -2px rgba(245, 217, 130, 0.18), 0 2px 6px 0 rgba(62, 65, 55, 0.03)`.
- **Frosted Translucency:** Floating action strips and bottom navigation bars use a backdrop blur (`backdrop-filter: blur(16px)`) paired with an 85% opacity `#FFFCF5` tint to let underlying content softly show through.

## Shapes

The design system embraces generous pill-shaped geometry and organic curves to create a welcoming, approachable feel.

- **Pill-Form Elements (`rounded-full`):** Applied to primary and secondary action buttons, status badges, filter chips, mood selector pills, and floating navigation bars.
- **Soft Interior Containers (`rounded-3xl` / `24px` to `32px`):** Used for primary card bodies, journal panels, hero message blocks, and elevated modal sheets.
- **Field & Control Insets (`rounded-2xl` / `16px`):** Applied to text inputs, search fields, and media playback control tiles.
- **Asymmetrical Chat Anchors:** Conversational chat bubbles pair wide rounded corners with a tapered anchor point to indicate dialogue flow (`20px 20px 6px 20px` for outgoing user notes; `20px 20px 20px 6px` for companion responses).

## Components

### Buttons
- **Primary CTA:** Fully rounded pill (`h-12` or `h-14`), filled with Buttery Yellow (`#F5D982`) and styled with Soft Charcoal typography (`#3E4137`, font weight 600). Includes subtle ambient golden glow on hover with a micro-scale shift (`scale(1.015)`).
- **Secondary / Action Ghost:** Pill outline with a 1px border (`#EAE3D5`), resting on a Cream White (`#FFFCF5`) surface with charcoal text. Shifts to Serene Teal (`#6FB5B5`) fill on hover.
- **Icon Buttons:** Circular 40px or 48px controls with centered 1.5px stroke icons, styled in Soft Charcoal against Cream White.

### Chips & Badges
- Fully rounded pills (`h-8` or `h-9`, horizontal padding `1rem`). Inactive chips use a Cream White background with a 1px `#EAE3D5` border and Warm Grey label. Active states transition to Serene Teal (`#6FB5B5`) with Charcoal text and an optional 6px colored mood dot.

### Cards & Surface Containers
- Built on Cream White (`#FFFCF5`) with a 1px `#EAE3D5` border and `rounded-3xl` corners.
- Internal padding follows `1.5rem` (`p-6`) for standard content and `1rem` (`p-4`) on compact mobile views.
- Resting cards sit directly on the ivory background; interactive cards gently lift with an ambient glow (`translateY(-2px)`) on hover.

### Inputs & Search Bars
- Shaped with `rounded-2xl` or full pill contours, using Cream White (`#FFFCF5`) backgrounds and a 1px `#EAE3D5` border.
- Placeholder copy is set in Warm Grey (`#89877F`).
- Focus states display a soft 2px outline ring in Buttery Yellow (`#F5D982`) with an offset of 2px, avoiding harsh blue browser defaults.

### Checkboxes & Radio Buttons
- **Radio Buttons / Mood Toggles:** Circular containers with a soft 1.5px border (`#EAE3D5`). When selected, they reveal an inner Buttery Yellow dot surrounded by a subtle warm aura.
- **Checkboxes:** Soft square forms with a 6px border radius (`#EAE3D5` border) that fill with Buttery Yellow and display a Charcoal checkmark upon selection.

### Mood Sliders & Audio Reflection Tiles
- Sliders use an 8px track filled with Pale Vanilla (`#FFF8E8`) with a completed track in Buttery Yellow (`#F5D982`). The thumb is a circular 28px ivory orb with a gentle ambient shadow.
- Audio and meditation tiles feature rounded play controls (`rounded-full`), clear duration metadata in Warm Grey, and subtle micro-illustrations.
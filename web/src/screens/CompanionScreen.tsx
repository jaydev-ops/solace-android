import React, { useState, useRef, useEffect } from 'react';
import { useSanctuary } from '../context/SanctuaryContext';
import {
  REGULATION_MODES,
  RegulationModeId,
  ChatMessage,
  SafetyQuestionId
} from '../types/solace';
import { AiService } from '../services/aiService';
import {
  Send,
  Sparkles,
  ShieldAlert,
  RotateCcw,
  Bot,
  User,
  Heart,
  Flower2,
  Wind,
  Brain,
  Compass,
  PhoneCall,
  Clock,
  Download,
  Trash2
} from 'lucide-react';

export const CompanionScreen: React.FC = () => {
  const {
    preferences,
    updatePreferences,
    activeMood,
    moods,
    journalEntries,
    chatMessages,
    setChatMessages,
    clearChat,
    setIsEmergencyModalOpen,
    setActiveScreen
  } = useSanctuary();

  const [activeMode, setActiveMode] = useState<RegulationModeId>('listen');
  const [inputText, setInputText] = useState('');
  const [isTyping, setIsTyping] = useState(false);
  const [pendingSafetyQuestion, setPendingSafetyQuestion] = useState<SafetyQuestionId | undefined>(
    undefined
  );

  const messagesEndRef = useRef<HTMLDivElement>(null);
  const modeConfig = REGULATION_MODES[activeMode];

  const chatbotRoles = [
    { id: 'companion', title: 'Sanctuary Companion', icon: Flower2 },
    { id: 'somatic', title: 'Somatic Guide', icon: Wind },
    { id: 'cbt', title: 'Thought Clarifier', icon: Brain },
    { id: 'wisdom', title: 'Wisdom Mirror', icon: Sparkles }
  ];

  const scrollToBottom = () => {
    messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
  };

  useEffect(() => {
    scrollToBottom();
  }, [chatMessages, isTyping]);

  // Initial welcome message if conversation is empty
  useEffect(() => {
    if (chatMessages.length === 0) {
      const name = preferences.sanctuaryName?.trim() || 'friend';
      const welcomeMessage: ChatMessage = {
        id: 'msg-welcome',
        role: 'assistant',
        text: `Welcome to your emotional sanctuary, ${name}. I am here to hold whatever is in your thoughts without judgment or rush.\n\nTake an unhurried breath. You can choose a regulation mode or speak whatever feels heavy today.`,
        timestamp: Date.now(),
        regulationMode: 'listen'
      };
      setChatMessages([welcomeMessage]);
    }
  }, []);

  const handleSendMessage = async (textToSend?: string) => {
    const messageContent = (textToSend !== undefined ? textToSend : inputText).trim();
    if (!messageContent || isTyping) return;

    setInputText('');

    const userMessage: ChatMessage = {
      id: 'msg-' + Date.now(),
      role: 'user',
      text: messageContent,
      timestamp: Date.now()
    };

    setChatMessages((prev) => [...prev, userMessage]);
    setIsTyping(true);

    try {
      const latestMood = moods[0]?.mood || activeMood;
      const latestValence = moods[0]?.intensity || 5;
      const latestJournal = journalEntries[0]?.content?.slice(0, 160);

      const result = await AiService.generateResponse(messageContent, {
        userName: preferences.sanctuaryName,
        apiKey: preferences.geminiApiKey,
        model: preferences.geminiModel,
        role: preferences.chatbotRole,
        regulationMode: activeMode,
        currentMood: latestMood,
        moodIntensity: latestValence,
        recentJournalSnippet: latestJournal,
        conversationHistory: chatMessages,
        pendingQuestionId: pendingSafetyQuestion
      });

      if (result.safety.requiresInterception && result.safety.questionId) {
        setPendingSafetyQuestion(result.safety.questionId);
      } else {
        setPendingSafetyQuestion(undefined);
      }

      const botMessage: ChatMessage = {
        id: 'msg-' + (Date.now() + 1),
        role: 'assistant',
        text: result.text,
        timestamp: Date.now(),
        riskLevel: result.safety.riskLevel,
        showEmergencyCard: result.safety.showEmergencyCard,
        regulationMode: activeMode
      };

      setChatMessages((prev) => [...prev, botMessage]);
    } catch (err) {
      console.error('Error generating AI response:', err);
      const fallbackMsg: ChatMessage = {
        id: 'msg-err-' + Date.now(),
        role: 'assistant',
        text: "I am right here with you. Take a gentle breath. Let's pause and hold space for a moment together.",
        timestamp: Date.now()
      };
      setChatMessages((prev) => [...prev, fallbackMsg]);
    } finally {
      setIsTyping(false);
    }
  };

  const handleExportChat = () => {
    const lines = chatMessages.map((m) => {
      const sender = m.role === 'user' ? (preferences.sanctuaryName || 'You') : 'Solace AI';
      const time = m.timestamp ? new Date(m.timestamp).toLocaleString() : '';
      return `[${time}] ${sender}:\n${m.text}\n`;
    });
    const content = `# Solace+ Conversation History\nUser: ${preferences.sanctuaryName || 'Sanctuary Friend'}\nExported: ${new Date().toLocaleString()}\nTotal Messages: ${chatMessages.length}\n\n---\n\n${lines.join('\n')}`;
    const blob = new Blob([content], { type: 'text/markdown;charset=utf-8' });
    const url = URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = `solace-chat-${(preferences.sanctuaryName || 'history').toLowerCase()}-${new Date().toISOString().slice(0, 10)}.md`;
    document.body.appendChild(a);
    a.click();
    document.body.removeChild(a);
    URL.revokeObjectURL(url);
  };

  const currentRoleObj = chatbotRoles.find((r) => r.id === preferences.chatbotRole) || chatbotRoles[0];

  return (
    <div
      style={{
        display: 'flex',
        flexDirection: 'column',
        height: 'calc(100dvh - 65px)',
        maxWidth: '680px',
        margin: '0 auto',
        width: '100%',
        padding: '8px 20px 80px',
        boxSizing: 'border-box'
      }}
    >
      {/* Scrollable Conversation & Header Section */}
      <div
        style={{
          flex: 1,
          overflowY: 'auto',
          display: 'flex',
          flexDirection: 'column',
          gap: '16px',
          paddingBottom: '16px'
        }}
      >
        {/* 1. Hero Sanctuary Badge (matches CompanionScreen.kt) */}
        <div
          className="solace-card"
          style={{
            padding: '18px 20px',
            display: 'flex',
            flexDirection: 'column',
            alignItems: 'center',
            textAlign: 'center'
          }}
        >
          <div
            style={{
              width: '52px',
              height: '52px',
              borderRadius: '50%',
              backgroundColor: 'var(--solace-accent)',
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'center',
              color: 'var(--solace-on-accent)',
              marginBottom: '10px'
            }}
          >
            <Sparkles size={28} />
          </div>

          <div style={{ display: 'flex', alignItems: 'center', gap: '6px' }}>
            <span style={{ fontSize: '1.1rem', fontWeight: 800, color: 'var(--solace-primary-text)' }}>
              solace AI
            </span>
            <span
              style={{
                fontSize: '9px',
                fontWeight: 800,
                letterSpacing: '0.5px',
                backgroundColor: 'var(--solace-accent-soft)',
                color: 'var(--solace-on-accent)',
                padding: '2px 6px',
                borderRadius: '6px',
                textTransform: 'uppercase'
              }}
            >
              Sanctuary
            </span>
          </div>

          <div style={{ display: 'flex', alignItems: 'center', gap: '6px', marginTop: '4px' }}>
            <span
              style={{
                width: '6px',
                height: '6px',
                borderRadius: '50%',
                backgroundColor: 'var(--active-mood-primary)'
              }}
            />
            <span style={{ fontSize: '11px', color: 'var(--solace-secondary-text)' }}>
              {preferences.geminiApiKey ? 'Gemini Engine' : 'Rule-Based Sanctuary'} • {currentRoleObj.title}
            </span>
          </div>
        </div>

        {/* 2. Chatbot Role Selector (matches CompanionScreen.kt) */}
        <div>
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '8px' }}>
            <span style={{ fontSize: '12px', fontWeight: 700, color: 'var(--solace-primary-text)' }}>
              Chatbot Role & Focus
            </span>
            <span style={{ fontSize: '11px', color: 'var(--solace-secondary-text)' }}>
              System role calibration
            </span>
          </div>

          <div style={{ display: 'flex', gap: '8px', overflowX: 'auto', paddingBottom: '4px', scrollbarWidth: 'none' }}>
            {chatbotRoles.map((role) => {
              const isSelected = preferences.chatbotRole === role.id;
              const Icon = role.icon;
              return (
                <button
                  key={role.id}
                  onClick={() => updatePreferences({ chatbotRole: role.id as any })}
                  style={{
                    display: 'flex',
                    alignItems: 'center',
                    gap: '6px',
                    padding: '7px 12px',
                    borderRadius: '16px',
                    backgroundColor: isSelected ? 'var(--solace-accent)' : 'var(--solace-input-bg)',
                    border: isSelected ? '1px solid transparent' : '1px solid var(--solace-border)',
                    color: isSelected ? 'var(--solace-on-accent)' : 'var(--solace-primary-text)',
                    fontSize: '12px',
                    fontWeight: 600,
                    cursor: 'pointer',
                    whiteSpace: 'nowrap'
                  }}
                >
                  <Icon size={14} />
                  <span>{role.title}</span>
                </button>
              );
            })}
          </div>
        </div>

        {/* 3. Regulation Mode Horizontal Selector */}
        <div>
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '8px' }}>
            <span style={{ fontSize: '12px', fontWeight: 700, color: 'var(--solace-primary-text)' }}>
              Regulation Mode
            </span>
            <button
              onClick={clearChat}
              className="btn-ghost"
              style={{ fontSize: '11px', padding: '2px 6px', color: 'var(--solace-secondary-text)' }}
            >
              <RotateCcw size={12} />
              <span>Reset</span>
            </button>
          </div>

          <div style={{ display: 'flex', gap: '6px', overflowX: 'auto', paddingBottom: '4px', scrollbarWidth: 'none' }}>
            {(Object.keys(REGULATION_MODES) as RegulationModeId[]).map((key) => {
              const mode = REGULATION_MODES[key];
              const isActive = activeMode === key;
              return (
                <button
                  key={key}
                  type="button"
                  onClick={() => setActiveMode(key)}
                  className={`tag-chip ${isActive ? 'selected' : ''}`}
                  style={{ padding: '5px 12px', fontSize: '12px', whiteSpace: 'nowrap' }}
                >
                  <span>{mode.iconEmoji}</span>
                  <span>{mode.title}</span>
                </button>
              );
            })}
          </div>
        </div>

        {/* Chat History Header Controls */}
        <div
          style={{
            display: 'flex',
            justifyContent: 'space-between',
            alignItems: 'center',
            padding: '8px 12px',
            backgroundColor: 'var(--solace-surface-raised)',
            borderRadius: '16px',
            border: '1px solid var(--solace-border)',
            fontSize: '11px',
            color: 'var(--solace-secondary-text)'
          }}
        >
          <div style={{ display: 'flex', alignItems: 'center', gap: '6px' }}>
            <Clock size={13} style={{ color: 'var(--solace-amber-primary)' }} />
            <span>
              <strong>{chatMessages.length}</strong> messages in history
            </span>
          </div>

          <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
            <button
              type="button"
              onClick={handleExportChat}
              className="btn-ghost"
              style={{
                padding: '4px 8px',
                fontSize: '11px',
                display: 'flex',
                alignItems: 'center',
                gap: '4px',
                borderRadius: '8px',
                color: 'var(--solace-primary-text)'
              }}
              title="Download conversation history"
            >
              <Download size={12} />
              <span>Export</span>
            </button>

            <button
              type="button"
              onClick={() => {
                if (confirm('Are you sure you want to clear your current conversation history? This cannot be undone.')) {
                  clearChat();
                }
              }}
              className="btn-ghost"
              style={{
                padding: '4px 8px',
                fontSize: '11px',
                display: 'flex',
                alignItems: 'center',
                gap: '4px',
                borderRadius: '8px',
                color: 'var(--solace-emergency)'
              }}
              title="Reset conversation"
            >
              <Trash2 size={12} />
              <span>Clear</span>
            </button>
          </div>
        </div>

        {/* 4. Chat Messages List */}
        <div style={{ display: 'flex', flexDirection: 'column', gap: '14px', marginTop: '4px' }}>
          {chatMessages.map((msg) => {
            const isUser = msg.role === 'user';
            const formattedTime = msg.timestamp
              ? new Date(msg.timestamp).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })
              : '';
            return (
              <div
                key={msg.id}
                style={{
                  display: 'flex',
                  flexDirection: 'column',
                  alignItems: isUser ? 'flex-end' : 'flex-start',
                  gap: '4px'
                }}
              >
                {/* Sender badge & timestamp */}
                <div
                  style={{
                    display: 'flex',
                    alignItems: 'center',
                    gap: '4px',
                    fontSize: '10px',
                    fontWeight: 600,
                    color: 'var(--solace-secondary-text)',
                    padding: '0 4px'
                  }}
                >
                  {!isUser && <span style={{ color: 'var(--solace-amber-primary)' }}>✨</span>}
                  <span>{isUser ? (preferences.sanctuaryName || 'You') : 'Solace AI'}</span>
                  {formattedTime && <span style={{ fontWeight: 400, opacity: 0.8 }}>• {formattedTime}</span>}
                </div>

                <div
                  className={`message-bubble ${isUser ? 'user' : 'assistant'}`}
                  style={{ whiteSpace: 'pre-wrap' }}
                >
                  {msg.text}
                </div>

                {/* Crisis helpline card if triggered */}
                {msg.showEmergencyCard && (
                  <div
                    className="solace-card"
                    style={{
                      marginTop: '6px',
                      padding: '14px',
                      borderColor: 'var(--solace-emergency)',
                      backgroundColor: 'var(--solace-emergency-soft)',
                      maxWidth: '85%'
                    }}
                  >
                    <div style={{ display: 'flex', alignItems: 'center', gap: '6px', color: 'var(--solace-emergency)', marginBottom: '4px' }}>
                      <ShieldAlert size={16} />
                      <strong style={{ fontSize: '0.85rem' }}>Immediate Care Available</strong>
                    </div>
                    <p style={{ fontSize: '0.8rem', color: 'var(--solace-primary-text)', margin: '0 0 8px' }}>
                      Free 24/7 counselors are ready right now:
                    </p>
                    <div style={{ display: 'flex', gap: '8px', flexWrap: 'wrap' }}>
                      <a
                        href="tel:14416"
                        className="btn-primary"
                        style={{
                          padding: '6px 12px',
                          fontSize: '0.78rem',
                          backgroundColor: 'var(--solace-emergency)',
                          color: '#FFF',
                          minHeight: '34px',
                          textDecoration: 'none'
                        }}
                      >
                        <PhoneCall size={13} />
                        <span>Tele-MANAS (14416)</span>
                      </a>
                      <button
                        onClick={() => setIsEmergencyModalOpen(true)}
                        className="btn-secondary"
                        style={{ padding: '6px 12px', fontSize: '0.78rem', minHeight: '34px' }}
                      >
                        View All
                      </button>
                    </div>
                  </div>
                )}
              </div>
            );
          })}

          {isTyping && (
            <div style={{ display: 'flex', alignItems: 'center', gap: '8px', padding: '4px 8px' }}>
              <span style={{ fontSize: '0.8rem', color: 'var(--solace-secondary-text)', fontStyle: 'italic' }}>
                Solace is holding space...
              </span>
              <div className="typing-dots">
                <span className="typing-dot" />
                <span className="typing-dot" />
                <span className="typing-dot" />
              </div>
            </div>
          )}

          <div ref={messagesEndRef} />
        </div>
      </div>

      {/* Starter Prompts */}
      <div style={{ display: 'flex', gap: '6px', overflowX: 'auto', padding: '6px 0', scrollbarWidth: 'none' }}>
        {modeConfig.starterPrompts.map((prompt, i) => (
          <button
            key={i}
            type="button"
            onClick={() => handleSendMessage(prompt)}
            className="btn-ghost"
            style={{
              padding: '6px 12px',
              borderRadius: 'var(--radius-pill)',
              backgroundColor: 'var(--solace-surface-raised)',
              border: '1px solid var(--solace-border)',
              fontSize: '11px',
              whiteSpace: 'nowrap'
            }}
          >
            {prompt}
          </button>
        ))}
      </div>

      {/* Bottom Input Field */}
      <form
        onSubmit={(e) => {
          e.preventDefault();
          handleSendMessage();
        }}
        style={{
          display: 'flex',
          alignItems: 'center',
          gap: '8px',
          paddingTop: '6px'
        }}
      >
        <button
          type="button"
          onClick={() => setActiveScreen('regulation')}
          className="btn-ghost"
          style={{
            padding: '10px',
            borderRadius: '50%',
            backgroundColor: 'var(--solace-surface-raised)',
            border: '1px solid var(--solace-border)'
          }}
          title="Open Somatic Breathing Pacer"
          aria-label="Open breathing exercise"
        >
          <Wind size={18} style={{ color: 'var(--active-mood-primary)' }} />
        </button>

        <input
          type="text"
          value={inputText}
          onChange={(e) => setInputText(e.target.value)}
          placeholder={modeConfig.promptPlaceholder}
          className="input-field"
          style={{ minHeight: '48px', borderRadius: '24px' }}
        />

        <button
          type="submit"
          disabled={!inputText.trim() || isTyping}
          style={{
            width: '48px',
            height: '48px',
            borderRadius: '50%',
            backgroundColor: 'var(--solace-accent)',
            color: 'var(--solace-on-accent)',
            border: 'none',
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            cursor: 'pointer',
            flexShrink: 0,
            opacity: !inputText.trim() || isTyping ? 0.45 : 1,
            transition: 'all 0.2s ease'
          }}
          aria-label="Send message"
        >
          <Send size={18} />
        </button>
      </form>
    </div>
  );
};

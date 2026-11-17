import React, { useState } from 'react';
import { useSanctuary } from '../context/SanctuaryContext';
import { MOOD_DEFINITIONS, MoodId, JournalEntry } from '../types/solace';
import {
  BookOpen,
  Plus,
  Search,
  Trash2,
  Tag,
  Sparkles,
  Calendar,
  X,
  Check
} from 'lucide-react';

const PROMPT_INSPIRATIONS = [
  'What is one feeling that needs gentle acceptance today, without trying to fix it?',
  'What gave your nervous system a small moment of comfort this week?',
  'If you were to treat yourself like someone you deeply love right now, what would you say?',
  'What is one quiet victory or effort you made that nobody else saw?',
  'What is something heavy you are ready to put down for the evening?'
];

export const JournalScreen: React.FC = () => {
  const { journalEntries, addJournalEntry, deleteJournalEntry, activeMood } = useSanctuary();

  const [searchQuery, setSearchQuery] = useState('');
  const [selectedMoodFilter, setSelectedMoodFilter] = useState<string>('all');
  const [isEditorOpen, setIsEditorOpen] = useState(false);

  // Editor states
  const [newTitle, setNewTitle] = useState('');
  const [newContent, setNewContent] = useState('');
  const [newMood, setNewMood] = useState<MoodId>(activeMood);
  const [newTagsInput, setNewTagsInput] = useState('');

  // Selected entry for viewing
  const [activeEntry, setActiveEntry] = useState<JournalEntry | null>(null);

  const filteredEntries = journalEntries.filter((entry) => {
    const matchesSearch =
      entry.title.toLowerCase().includes(searchQuery.toLowerCase()) ||
      entry.content.toLowerCase().includes(searchQuery.toLowerCase()) ||
      entry.tags.some((t) => t.toLowerCase().includes(searchQuery.toLowerCase()));

    const matchesMood =
      selectedMoodFilter === 'all' || entry.associatedMood === selectedMoodFilter;

    return matchesSearch && matchesMood;
  });

  const handleSaveEntry = (e: React.FormEvent) => {
    e.preventDefault();
    if (!newContent.trim()) return;

    const tags = newTagsInput
      .split(',')
      .map((t) => t.trim())
      .filter(Boolean);

    addJournalEntry(newTitle, newContent, newMood, tags);

    // Reset & close
    setNewTitle('');
    setNewContent('');
    setNewTagsInput('');
    setIsEditorOpen(false);
  };

  const wordCount = newContent.trim().split(/\s+/).filter(Boolean).length;

  return (
    <div
      style={{
        maxWidth: '680px',
        margin: '0 auto',
        width: '100%',
        padding: '8px 20px 100px',
        display: 'flex',
        flexDirection: 'column',
        gap: '20px'
      }}
    >
      {/* Top Header */}
      <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', flexWrap: 'wrap', gap: '12px' }}>
        <div>
          <h1 style={{ fontSize: '1.85rem', fontWeight: 800, margin: 0 }}>Private Journal</h1>
          <p style={{ fontSize: '0.92rem', color: 'var(--solace-secondary-text)', marginTop: '4px' }}>
            Encrypted locally. A safe harbor for your reflections and thoughts.
          </p>
        </div>

        <button
          onClick={() => setIsEditorOpen(true)}
          className="btn-primary"
          style={{ minHeight: '44px', padding: '10px 18px' }}
        >
          <Plus size={18} />
          <span>New Reflection</span>
        </button>
      </div>

      {/* Search and Filters */}
      <div style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
        <div style={{ position: 'relative' }}>
          <Search
            size={18}
            style={{
              position: 'absolute',
              left: '16px',
              top: '50%',
              transform: 'translateY(-50%)',
              color: 'var(--solace-muted-text)'
            }}
          />
          <input
            type="search"
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            placeholder="Search through your entries or tags..."
            className="input-field"
            style={{ paddingLeft: '44px', borderRadius: 'var(--radius-pill)', minHeight: '46px' }}
          />
        </div>

        {/* Mood Filter Chips */}
        <div style={{ display: 'flex', gap: '6px', overflowX: 'auto', paddingBottom: '4px', scrollbarWidth: 'none' }}>
          <button
            onClick={() => setSelectedMoodFilter('all')}
            className={`tag-chip ${selectedMoodFilter === 'all' ? 'selected' : ''}`}
          >
            <span>All Entries ({journalEntries.length})</span>
          </button>
          {(Object.keys(MOOD_DEFINITIONS) as MoodId[]).map((key) => {
            const def = MOOD_DEFINITIONS[key];
            const isSelected = selectedMoodFilter === key;
            return (
              <button
                key={key}
                onClick={() => setSelectedMoodFilter(key)}
                className={`tag-chip ${isSelected ? 'selected' : ''}`}
              >
                <span>{def.emoji}</span>
                <span>{def.title}</span>
              </button>
            );
          })}
        </div>
      </div>

      {/* New Reflection Editor Modal */}
      {isEditorOpen && (
        <div
          style={{
            position: 'fixed',
            inset: 0,
            backgroundColor: 'rgba(23, 23, 20, 0.65)',
            backdropFilter: 'blur(10px)',
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            zIndex: 1000,
            padding: '20px'
          }}
        >
          <div
            className="solace-card"
            style={{
              maxWidth: '620px',
              width: '100%',
              maxHeight: '90vh',
              overflowY: 'auto',
              padding: '28px 24px',
              display: 'flex',
              flexDirection: 'column',
              gap: '16px'
            }}
          >
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
              <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
                <BookOpen size={20} style={{ color: 'var(--active-mood-primary)' }} />
                <h3 style={{ fontSize: '1.2rem', fontWeight: 800, margin: 0 }}>New Reflection</h3>
              </div>
              <button onClick={() => setIsEditorOpen(false)} className="btn-ghost" style={{ padding: '4px' }}>
                <X size={20} />
              </button>
            </div>

            {/* Prompt Inspiration Card */}
            <div
              style={{
                backgroundColor: 'var(--solace-surface-raised)',
                border: '1px solid var(--solace-border)',
                padding: '12px 16px',
                borderRadius: 'var(--radius-md)',
                fontSize: '0.85rem',
                color: 'var(--solace-secondary-text)'
              }}
            >
              <div style={{ display: 'flex', alignItems: 'center', gap: '6px', fontWeight: 600, color: 'var(--active-mood-primary)', marginBottom: '4px' }}>
                <Sparkles size={14} />
                <span>Prompt Inspiration</span>
              </div>
              "{PROMPT_INSPIRATIONS[Math.floor(Math.random() * PROMPT_INSPIRATIONS.length)]}"
            </div>

            <form onSubmit={handleSaveEntry} style={{ display: 'flex', flexDirection: 'column', gap: '14px' }}>
              <input
                type="text"
                value={newTitle}
                onChange={(e) => setNewTitle(e.target.value)}
                placeholder="Title (or leave blank for date title)..."
                className="input-field"
                style={{ fontWeight: 700, fontSize: '1.05rem' }}
              />

              <div style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
                <span style={{ fontSize: '0.82rem', color: 'var(--solace-secondary-text)' }}>Associated Mood:</span>
                <select
                  value={newMood}
                  onChange={(e) => setNewMood(e.target.value as MoodId)}
                  className="input-field"
                  style={{ width: 'auto', padding: '6px 12px', fontSize: '0.85rem' }}
                >
                  {(Object.keys(MOOD_DEFINITIONS) as MoodId[]).map((m) => (
                    <option key={m} value={m}>
                      {MOOD_DEFINITIONS[m].emoji} {MOOD_DEFINITIONS[m].title}
                    </option>
                  ))}
                </select>
              </div>

              <div>
                <textarea
                  value={newContent}
                  onChange={(e) => setNewContent(e.target.value)}
                  placeholder="Spill whatever thoughts, worries, or gentle realizations are present..."
                  className="input-field"
                  rows={8}
                  required
                />
                <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: '0.75rem', color: 'var(--solace-secondary-text)', marginTop: '4px' }}>
                  <span>{wordCount} words</span>
                  <span>Private Local Storage</span>
                </div>
              </div>

              <input
                type="text"
                value={newTagsInput}
                onChange={(e) => setNewTagsInput(e.target.value)}
                placeholder="Tags separated by commas (e.g. Work, Morning quiet, Gratitude)..."
                className="input-field"
                style={{ fontSize: '0.85rem' }}
              />

              <div style={{ display: 'flex', justifyContent: 'flex-end', gap: '10px', marginTop: '8px' }}>
                <button
                  type="button"
                  onClick={() => setIsEditorOpen(false)}
                  className="btn-ghost"
                >
                  Cancel
                </button>
                <button type="submit" className="btn-primary">
                  <Check size={18} />
                  <span>Save to Journal</span>
                </button>
              </div>
            </form>
          </div>
        </div>
      )}

      {/* Entry Detail Modal */}
      {activeEntry && (
        <div
          style={{
            position: 'fixed',
            inset: 0,
            backgroundColor: 'rgba(23, 23, 20, 0.65)',
            backdropFilter: 'blur(10px)',
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            zIndex: 1000,
            padding: '20px'
          }}
        >
          <div
            className="solace-card"
            style={{
              maxWidth: '640px',
              width: '100%',
              maxHeight: '90vh',
              overflowY: 'auto',
              padding: '28px 24px',
              display: 'flex',
              flexDirection: 'column',
              gap: '16px'
            }}
          >
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start' }}>
              <div>
                <div style={{ display: 'flex', alignItems: 'center', gap: '8px', marginBottom: '6px' }}>
                  {activeEntry.associatedMood && (
                    <span
                      style={{
                        fontSize: '0.75rem',
                        fontWeight: 600,
                        padding: '2px 8px',
                        borderRadius: 'var(--radius-pill)',
                        backgroundColor: 'var(--active-mood-bg)',
                        color: 'var(--active-mood-primary)',
                        border: '1px solid var(--active-mood-border)'
                      }}
                    >
                      {MOOD_DEFINITIONS[activeEntry.associatedMood]?.emoji}{' '}
                      {MOOD_DEFINITIONS[activeEntry.associatedMood]?.title}
                    </span>
                  )}
                  <span style={{ fontSize: '0.78rem', color: 'var(--solace-secondary-text)' }}>
                    {new Date(activeEntry.timestamp).toLocaleDateString(undefined, {
                      weekday: 'short',
                      month: 'short',
                      day: 'numeric',
                      hour: '2-digit',
                      minute: '2-digit'
                    })}
                  </span>
                </div>
                <h2 style={{ fontSize: '1.4rem', fontWeight: 800, margin: 0 }}>
                  {activeEntry.title}
                </h2>
              </div>
              <button onClick={() => setActiveEntry(null)} className="btn-ghost" style={{ padding: '4px' }}>
                <X size={20} />
              </button>
            </div>

            <p style={{ fontSize: '0.98rem', lineHeight: 1.7, color: 'var(--solace-primary-text)', whiteSpace: 'pre-wrap', margin: '8px 0' }}>
              {activeEntry.content}
            </p>

            {activeEntry.tags.length > 0 && (
              <div style={{ display: 'flex', flexWrap: 'wrap', gap: '6px', marginTop: '8px' }}>
                {activeEntry.tags.map((t, idx) => (
                  <span key={idx} className="tag-chip" style={{ fontSize: '0.75rem' }}>
                    #{t}
                  </span>
                ))}
              </div>
            )}

            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginTop: '16px', paddingTop: '16px', borderTop: '1px solid var(--solace-border)' }}>
              <button
                type="button"
                onClick={() => {
                  if (confirm('Are you sure you want to delete this reflection?')) {
                    deleteJournalEntry(activeEntry.id);
                    setActiveEntry(null);
                  }
                }}
                className="btn-ghost"
                style={{ color: 'var(--solace-emergency)', fontSize: '0.82rem' }}
              >
                <Trash2 size={16} />
                <span>Delete Entry</span>
              </button>
              <button
                type="button"
                onClick={() => setActiveEntry(null)}
                className="btn-secondary"
                style={{ minHeight: '38px', padding: '6px 16px' }}
              >
                Close
              </button>
            </div>
          </div>
        </div>
      )}

      {/* Entries List */}
      <div style={{ display: 'flex', flexDirection: 'column', gap: '14px' }}>
        {filteredEntries.length === 0 ? (
          <div
            className="solace-card"
            style={{
              padding: '40px 20px',
              textAlign: 'center',
              display: 'flex',
              flexDirection: 'column',
              alignItems: 'center',
              gap: '12px'
            }}
          >
            <BookOpen size={36} style={{ color: 'var(--solace-muted-text)' }} />
            <h3 style={{ fontSize: '1.1rem', fontWeight: 700, margin: 0 }}>
              No reflections found
            </h3>
            <p style={{ fontSize: '0.85rem', color: 'var(--solace-secondary-text)', maxWidth: '400px', margin: 0 }}>
              This private harbor is ready whenever you want to spill your thoughts.
            </p>
            <button
              onClick={() => setIsEditorOpen(true)}
              className="btn-primary"
              style={{ marginTop: '8px' }}
            >
              Write First Reflection
            </button>
          </div>
        ) : (
          filteredEntries.map((entry) => (
            <div
              key={entry.id}
              onClick={() => setActiveEntry(entry)}
              className="solace-card interactive"
              style={{ padding: '20px', display: 'flex', flexDirection: 'column', gap: '8px' }}
            >
              <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
                <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
                  {entry.associatedMood && (
                    <span
                      style={{
                        fontSize: '0.72rem',
                        fontWeight: 600,
                        padding: '2px 8px',
                        borderRadius: 'var(--radius-pill)',
                        backgroundColor: 'var(--active-mood-bg)',
                        color: 'var(--active-mood-primary)',
                        border: '1px solid var(--active-mood-border)'
                      }}
                    >
                      {MOOD_DEFINITIONS[entry.associatedMood]?.emoji}{' '}
                      {MOOD_DEFINITIONS[entry.associatedMood]?.title}
                    </span>
                  )}
                  <span style={{ fontSize: '0.75rem', color: 'var(--solace-secondary-text)' }}>
                    {new Date(entry.timestamp).toLocaleDateString(undefined, {
                      month: 'short',
                      day: 'numeric',
                      year: 'numeric'
                    })}
                  </span>
                </div>
                <span style={{ fontSize: '0.72rem', color: 'var(--solace-muted-text)' }}>
                  {entry.wordCount} words
                </span>
              </div>

              <h3 style={{ fontSize: '1.1rem', fontWeight: 700, margin: '2px 0 0' }}>
                {entry.title}
              </h3>

              <p
                style={{
                  fontSize: '0.88rem',
                  color: 'var(--solace-secondary-text)',
                  lineHeight: 1.5,
                  margin: 0,
                  display: '-webkit-box',
                  WebkitLineClamp: 2,
                  WebkitBoxOrient: 'vertical',
                  overflow: 'hidden'
                }}
              >
                {entry.content}
              </p>

              {entry.tags.length > 0 && (
                <div style={{ display: 'flex', flexWrap: 'wrap', gap: '6px', marginTop: '6px' }}>
                  {entry.tags.map((t, idx) => (
                    <span key={idx} className="tag-chip" style={{ fontSize: '0.72rem', padding: '3px 8px' }}>
                      #{t}
                    </span>
                  ))}
                </div>
              )}
            </div>
          ))
        )}
      </div>
    </div>
  );
};

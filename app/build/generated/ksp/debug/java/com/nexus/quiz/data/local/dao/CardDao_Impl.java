package com.nexus.quiz.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.nexus.quiz.data.local.Converters;
import com.nexus.quiz.data.local.entity.CardEntity;
import com.nexus.quiz.data.model.CardType;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class CardDao_Impl implements CardDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<CardEntity> __insertionAdapterOfCardEntity;

  private final Converters __converters = new Converters();

  private final EntityDeletionOrUpdateAdapter<CardEntity> __deletionAdapterOfCardEntity;

  private final EntityDeletionOrUpdateAdapter<CardEntity> __updateAdapterOfCardEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteCardById;

  public CardDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfCardEntity = new EntityInsertionAdapter<CardEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `cards` (`id`,`deckId`,`type`,`prompt`,`answer`,`explanation`,`latexFormula`,`clozeOcclusion`,`derivationStepsJson`,`keyTermsJson`,`misconceptionsJson`,`difficulty`,`interval`,`repetitions`,`easeFactor`,`lapses`,`dueDate`,`lastReviewedDate`,`createdAt`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final CardEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getDeckId());
        final String _tmp = __converters.fromCardType(entity.getType());
        statement.bindString(3, _tmp);
        statement.bindString(4, entity.getPrompt());
        statement.bindString(5, entity.getAnswer());
        statement.bindString(6, entity.getExplanation());
        if (entity.getLatexFormula() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getLatexFormula());
        }
        if (entity.getClozeOcclusion() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getClozeOcclusion());
        }
        if (entity.getDerivationStepsJson() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getDerivationStepsJson());
        }
        if (entity.getKeyTermsJson() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getKeyTermsJson());
        }
        if (entity.getMisconceptionsJson() == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, entity.getMisconceptionsJson());
        }
        statement.bindString(12, entity.getDifficulty());
        statement.bindLong(13, entity.getInterval());
        statement.bindLong(14, entity.getRepetitions());
        statement.bindDouble(15, entity.getEaseFactor());
        statement.bindLong(16, entity.getLapses());
        statement.bindLong(17, entity.getDueDate());
        if (entity.getLastReviewedDate() == null) {
          statement.bindNull(18);
        } else {
          statement.bindLong(18, entity.getLastReviewedDate());
        }
        statement.bindLong(19, entity.getCreatedAt());
      }
    };
    this.__deletionAdapterOfCardEntity = new EntityDeletionOrUpdateAdapter<CardEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `cards` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final CardEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfCardEntity = new EntityDeletionOrUpdateAdapter<CardEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `cards` SET `id` = ?,`deckId` = ?,`type` = ?,`prompt` = ?,`answer` = ?,`explanation` = ?,`latexFormula` = ?,`clozeOcclusion` = ?,`derivationStepsJson` = ?,`keyTermsJson` = ?,`misconceptionsJson` = ?,`difficulty` = ?,`interval` = ?,`repetitions` = ?,`easeFactor` = ?,`lapses` = ?,`dueDate` = ?,`lastReviewedDate` = ?,`createdAt` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final CardEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getDeckId());
        final String _tmp = __converters.fromCardType(entity.getType());
        statement.bindString(3, _tmp);
        statement.bindString(4, entity.getPrompt());
        statement.bindString(5, entity.getAnswer());
        statement.bindString(6, entity.getExplanation());
        if (entity.getLatexFormula() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getLatexFormula());
        }
        if (entity.getClozeOcclusion() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getClozeOcclusion());
        }
        if (entity.getDerivationStepsJson() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getDerivationStepsJson());
        }
        if (entity.getKeyTermsJson() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getKeyTermsJson());
        }
        if (entity.getMisconceptionsJson() == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, entity.getMisconceptionsJson());
        }
        statement.bindString(12, entity.getDifficulty());
        statement.bindLong(13, entity.getInterval());
        statement.bindLong(14, entity.getRepetitions());
        statement.bindDouble(15, entity.getEaseFactor());
        statement.bindLong(16, entity.getLapses());
        statement.bindLong(17, entity.getDueDate());
        if (entity.getLastReviewedDate() == null) {
          statement.bindNull(18);
        } else {
          statement.bindLong(18, entity.getLastReviewedDate());
        }
        statement.bindLong(19, entity.getCreatedAt());
        statement.bindLong(20, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteCardById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM cards WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertCard(final CardEntity card, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfCardEntity.insertAndReturnId(card);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertCards(final List<CardEntity> cards,
      final Continuation<? super List<Long>> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<List<Long>>() {
      @Override
      @NonNull
      public List<Long> call() throws Exception {
        __db.beginTransaction();
        try {
          final List<Long> _result = __insertionAdapterOfCardEntity.insertAndReturnIdsList(cards);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteCard(final CardEntity card, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfCardEntity.handle(card);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateCard(final CardEntity card, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfCardEntity.handle(card);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteCardById(final long cardId, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteCardById.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, cardId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteCardById.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<CardEntity>> getCardsForDeck(final long deckId) {
    final String _sql = "SELECT * FROM cards WHERE deckId = ? ORDER BY createdAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, deckId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"cards"}, new Callable<List<CardEntity>>() {
      @Override
      @NonNull
      public List<CardEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDeckId = CursorUtil.getColumnIndexOrThrow(_cursor, "deckId");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfPrompt = CursorUtil.getColumnIndexOrThrow(_cursor, "prompt");
          final int _cursorIndexOfAnswer = CursorUtil.getColumnIndexOrThrow(_cursor, "answer");
          final int _cursorIndexOfExplanation = CursorUtil.getColumnIndexOrThrow(_cursor, "explanation");
          final int _cursorIndexOfLatexFormula = CursorUtil.getColumnIndexOrThrow(_cursor, "latexFormula");
          final int _cursorIndexOfClozeOcclusion = CursorUtil.getColumnIndexOrThrow(_cursor, "clozeOcclusion");
          final int _cursorIndexOfDerivationStepsJson = CursorUtil.getColumnIndexOrThrow(_cursor, "derivationStepsJson");
          final int _cursorIndexOfKeyTermsJson = CursorUtil.getColumnIndexOrThrow(_cursor, "keyTermsJson");
          final int _cursorIndexOfMisconceptionsJson = CursorUtil.getColumnIndexOrThrow(_cursor, "misconceptionsJson");
          final int _cursorIndexOfDifficulty = CursorUtil.getColumnIndexOrThrow(_cursor, "difficulty");
          final int _cursorIndexOfInterval = CursorUtil.getColumnIndexOrThrow(_cursor, "interval");
          final int _cursorIndexOfRepetitions = CursorUtil.getColumnIndexOrThrow(_cursor, "repetitions");
          final int _cursorIndexOfEaseFactor = CursorUtil.getColumnIndexOrThrow(_cursor, "easeFactor");
          final int _cursorIndexOfLapses = CursorUtil.getColumnIndexOrThrow(_cursor, "lapses");
          final int _cursorIndexOfDueDate = CursorUtil.getColumnIndexOrThrow(_cursor, "dueDate");
          final int _cursorIndexOfLastReviewedDate = CursorUtil.getColumnIndexOrThrow(_cursor, "lastReviewedDate");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<CardEntity> _result = new ArrayList<CardEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final CardEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpDeckId;
            _tmpDeckId = _cursor.getLong(_cursorIndexOfDeckId);
            final CardType _tmpType;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfType);
            _tmpType = __converters.toCardType(_tmp);
            final String _tmpPrompt;
            _tmpPrompt = _cursor.getString(_cursorIndexOfPrompt);
            final String _tmpAnswer;
            _tmpAnswer = _cursor.getString(_cursorIndexOfAnswer);
            final String _tmpExplanation;
            _tmpExplanation = _cursor.getString(_cursorIndexOfExplanation);
            final String _tmpLatexFormula;
            if (_cursor.isNull(_cursorIndexOfLatexFormula)) {
              _tmpLatexFormula = null;
            } else {
              _tmpLatexFormula = _cursor.getString(_cursorIndexOfLatexFormula);
            }
            final String _tmpClozeOcclusion;
            if (_cursor.isNull(_cursorIndexOfClozeOcclusion)) {
              _tmpClozeOcclusion = null;
            } else {
              _tmpClozeOcclusion = _cursor.getString(_cursorIndexOfClozeOcclusion);
            }
            final String _tmpDerivationStepsJson;
            if (_cursor.isNull(_cursorIndexOfDerivationStepsJson)) {
              _tmpDerivationStepsJson = null;
            } else {
              _tmpDerivationStepsJson = _cursor.getString(_cursorIndexOfDerivationStepsJson);
            }
            final String _tmpKeyTermsJson;
            if (_cursor.isNull(_cursorIndexOfKeyTermsJson)) {
              _tmpKeyTermsJson = null;
            } else {
              _tmpKeyTermsJson = _cursor.getString(_cursorIndexOfKeyTermsJson);
            }
            final String _tmpMisconceptionsJson;
            if (_cursor.isNull(_cursorIndexOfMisconceptionsJson)) {
              _tmpMisconceptionsJson = null;
            } else {
              _tmpMisconceptionsJson = _cursor.getString(_cursorIndexOfMisconceptionsJson);
            }
            final String _tmpDifficulty;
            _tmpDifficulty = _cursor.getString(_cursorIndexOfDifficulty);
            final int _tmpInterval;
            _tmpInterval = _cursor.getInt(_cursorIndexOfInterval);
            final int _tmpRepetitions;
            _tmpRepetitions = _cursor.getInt(_cursorIndexOfRepetitions);
            final float _tmpEaseFactor;
            _tmpEaseFactor = _cursor.getFloat(_cursorIndexOfEaseFactor);
            final int _tmpLapses;
            _tmpLapses = _cursor.getInt(_cursorIndexOfLapses);
            final long _tmpDueDate;
            _tmpDueDate = _cursor.getLong(_cursorIndexOfDueDate);
            final Long _tmpLastReviewedDate;
            if (_cursor.isNull(_cursorIndexOfLastReviewedDate)) {
              _tmpLastReviewedDate = null;
            } else {
              _tmpLastReviewedDate = _cursor.getLong(_cursorIndexOfLastReviewedDate);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new CardEntity(_tmpId,_tmpDeckId,_tmpType,_tmpPrompt,_tmpAnswer,_tmpExplanation,_tmpLatexFormula,_tmpClozeOcclusion,_tmpDerivationStepsJson,_tmpKeyTermsJson,_tmpMisconceptionsJson,_tmpDifficulty,_tmpInterval,_tmpRepetitions,_tmpEaseFactor,_tmpLapses,_tmpDueDate,_tmpLastReviewedDate,_tmpCreatedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getCardById(final long cardId, final Continuation<? super CardEntity> $completion) {
    final String _sql = "SELECT * FROM cards WHERE id = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, cardId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<CardEntity>() {
      @Override
      @Nullable
      public CardEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDeckId = CursorUtil.getColumnIndexOrThrow(_cursor, "deckId");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfPrompt = CursorUtil.getColumnIndexOrThrow(_cursor, "prompt");
          final int _cursorIndexOfAnswer = CursorUtil.getColumnIndexOrThrow(_cursor, "answer");
          final int _cursorIndexOfExplanation = CursorUtil.getColumnIndexOrThrow(_cursor, "explanation");
          final int _cursorIndexOfLatexFormula = CursorUtil.getColumnIndexOrThrow(_cursor, "latexFormula");
          final int _cursorIndexOfClozeOcclusion = CursorUtil.getColumnIndexOrThrow(_cursor, "clozeOcclusion");
          final int _cursorIndexOfDerivationStepsJson = CursorUtil.getColumnIndexOrThrow(_cursor, "derivationStepsJson");
          final int _cursorIndexOfKeyTermsJson = CursorUtil.getColumnIndexOrThrow(_cursor, "keyTermsJson");
          final int _cursorIndexOfMisconceptionsJson = CursorUtil.getColumnIndexOrThrow(_cursor, "misconceptionsJson");
          final int _cursorIndexOfDifficulty = CursorUtil.getColumnIndexOrThrow(_cursor, "difficulty");
          final int _cursorIndexOfInterval = CursorUtil.getColumnIndexOrThrow(_cursor, "interval");
          final int _cursorIndexOfRepetitions = CursorUtil.getColumnIndexOrThrow(_cursor, "repetitions");
          final int _cursorIndexOfEaseFactor = CursorUtil.getColumnIndexOrThrow(_cursor, "easeFactor");
          final int _cursorIndexOfLapses = CursorUtil.getColumnIndexOrThrow(_cursor, "lapses");
          final int _cursorIndexOfDueDate = CursorUtil.getColumnIndexOrThrow(_cursor, "dueDate");
          final int _cursorIndexOfLastReviewedDate = CursorUtil.getColumnIndexOrThrow(_cursor, "lastReviewedDate");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final CardEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpDeckId;
            _tmpDeckId = _cursor.getLong(_cursorIndexOfDeckId);
            final CardType _tmpType;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfType);
            _tmpType = __converters.toCardType(_tmp);
            final String _tmpPrompt;
            _tmpPrompt = _cursor.getString(_cursorIndexOfPrompt);
            final String _tmpAnswer;
            _tmpAnswer = _cursor.getString(_cursorIndexOfAnswer);
            final String _tmpExplanation;
            _tmpExplanation = _cursor.getString(_cursorIndexOfExplanation);
            final String _tmpLatexFormula;
            if (_cursor.isNull(_cursorIndexOfLatexFormula)) {
              _tmpLatexFormula = null;
            } else {
              _tmpLatexFormula = _cursor.getString(_cursorIndexOfLatexFormula);
            }
            final String _tmpClozeOcclusion;
            if (_cursor.isNull(_cursorIndexOfClozeOcclusion)) {
              _tmpClozeOcclusion = null;
            } else {
              _tmpClozeOcclusion = _cursor.getString(_cursorIndexOfClozeOcclusion);
            }
            final String _tmpDerivationStepsJson;
            if (_cursor.isNull(_cursorIndexOfDerivationStepsJson)) {
              _tmpDerivationStepsJson = null;
            } else {
              _tmpDerivationStepsJson = _cursor.getString(_cursorIndexOfDerivationStepsJson);
            }
            final String _tmpKeyTermsJson;
            if (_cursor.isNull(_cursorIndexOfKeyTermsJson)) {
              _tmpKeyTermsJson = null;
            } else {
              _tmpKeyTermsJson = _cursor.getString(_cursorIndexOfKeyTermsJson);
            }
            final String _tmpMisconceptionsJson;
            if (_cursor.isNull(_cursorIndexOfMisconceptionsJson)) {
              _tmpMisconceptionsJson = null;
            } else {
              _tmpMisconceptionsJson = _cursor.getString(_cursorIndexOfMisconceptionsJson);
            }
            final String _tmpDifficulty;
            _tmpDifficulty = _cursor.getString(_cursorIndexOfDifficulty);
            final int _tmpInterval;
            _tmpInterval = _cursor.getInt(_cursorIndexOfInterval);
            final int _tmpRepetitions;
            _tmpRepetitions = _cursor.getInt(_cursorIndexOfRepetitions);
            final float _tmpEaseFactor;
            _tmpEaseFactor = _cursor.getFloat(_cursorIndexOfEaseFactor);
            final int _tmpLapses;
            _tmpLapses = _cursor.getInt(_cursorIndexOfLapses);
            final long _tmpDueDate;
            _tmpDueDate = _cursor.getLong(_cursorIndexOfDueDate);
            final Long _tmpLastReviewedDate;
            if (_cursor.isNull(_cursorIndexOfLastReviewedDate)) {
              _tmpLastReviewedDate = null;
            } else {
              _tmpLastReviewedDate = _cursor.getLong(_cursorIndexOfLastReviewedDate);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _result = new CardEntity(_tmpId,_tmpDeckId,_tmpType,_tmpPrompt,_tmpAnswer,_tmpExplanation,_tmpLatexFormula,_tmpClozeOcclusion,_tmpDerivationStepsJson,_tmpKeyTermsJson,_tmpMisconceptionsJson,_tmpDifficulty,_tmpInterval,_tmpRepetitions,_tmpEaseFactor,_tmpLapses,_tmpDueDate,_tmpLastReviewedDate,_tmpCreatedAt);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<CardEntity>> getAllDueCards(final long currentTimestamp) {
    final String _sql = "SELECT * FROM cards WHERE dueDate <= ? ORDER BY dueDate ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, currentTimestamp);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"cards"}, new Callable<List<CardEntity>>() {
      @Override
      @NonNull
      public List<CardEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDeckId = CursorUtil.getColumnIndexOrThrow(_cursor, "deckId");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfPrompt = CursorUtil.getColumnIndexOrThrow(_cursor, "prompt");
          final int _cursorIndexOfAnswer = CursorUtil.getColumnIndexOrThrow(_cursor, "answer");
          final int _cursorIndexOfExplanation = CursorUtil.getColumnIndexOrThrow(_cursor, "explanation");
          final int _cursorIndexOfLatexFormula = CursorUtil.getColumnIndexOrThrow(_cursor, "latexFormula");
          final int _cursorIndexOfClozeOcclusion = CursorUtil.getColumnIndexOrThrow(_cursor, "clozeOcclusion");
          final int _cursorIndexOfDerivationStepsJson = CursorUtil.getColumnIndexOrThrow(_cursor, "derivationStepsJson");
          final int _cursorIndexOfKeyTermsJson = CursorUtil.getColumnIndexOrThrow(_cursor, "keyTermsJson");
          final int _cursorIndexOfMisconceptionsJson = CursorUtil.getColumnIndexOrThrow(_cursor, "misconceptionsJson");
          final int _cursorIndexOfDifficulty = CursorUtil.getColumnIndexOrThrow(_cursor, "difficulty");
          final int _cursorIndexOfInterval = CursorUtil.getColumnIndexOrThrow(_cursor, "interval");
          final int _cursorIndexOfRepetitions = CursorUtil.getColumnIndexOrThrow(_cursor, "repetitions");
          final int _cursorIndexOfEaseFactor = CursorUtil.getColumnIndexOrThrow(_cursor, "easeFactor");
          final int _cursorIndexOfLapses = CursorUtil.getColumnIndexOrThrow(_cursor, "lapses");
          final int _cursorIndexOfDueDate = CursorUtil.getColumnIndexOrThrow(_cursor, "dueDate");
          final int _cursorIndexOfLastReviewedDate = CursorUtil.getColumnIndexOrThrow(_cursor, "lastReviewedDate");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<CardEntity> _result = new ArrayList<CardEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final CardEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpDeckId;
            _tmpDeckId = _cursor.getLong(_cursorIndexOfDeckId);
            final CardType _tmpType;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfType);
            _tmpType = __converters.toCardType(_tmp);
            final String _tmpPrompt;
            _tmpPrompt = _cursor.getString(_cursorIndexOfPrompt);
            final String _tmpAnswer;
            _tmpAnswer = _cursor.getString(_cursorIndexOfAnswer);
            final String _tmpExplanation;
            _tmpExplanation = _cursor.getString(_cursorIndexOfExplanation);
            final String _tmpLatexFormula;
            if (_cursor.isNull(_cursorIndexOfLatexFormula)) {
              _tmpLatexFormula = null;
            } else {
              _tmpLatexFormula = _cursor.getString(_cursorIndexOfLatexFormula);
            }
            final String _tmpClozeOcclusion;
            if (_cursor.isNull(_cursorIndexOfClozeOcclusion)) {
              _tmpClozeOcclusion = null;
            } else {
              _tmpClozeOcclusion = _cursor.getString(_cursorIndexOfClozeOcclusion);
            }
            final String _tmpDerivationStepsJson;
            if (_cursor.isNull(_cursorIndexOfDerivationStepsJson)) {
              _tmpDerivationStepsJson = null;
            } else {
              _tmpDerivationStepsJson = _cursor.getString(_cursorIndexOfDerivationStepsJson);
            }
            final String _tmpKeyTermsJson;
            if (_cursor.isNull(_cursorIndexOfKeyTermsJson)) {
              _tmpKeyTermsJson = null;
            } else {
              _tmpKeyTermsJson = _cursor.getString(_cursorIndexOfKeyTermsJson);
            }
            final String _tmpMisconceptionsJson;
            if (_cursor.isNull(_cursorIndexOfMisconceptionsJson)) {
              _tmpMisconceptionsJson = null;
            } else {
              _tmpMisconceptionsJson = _cursor.getString(_cursorIndexOfMisconceptionsJson);
            }
            final String _tmpDifficulty;
            _tmpDifficulty = _cursor.getString(_cursorIndexOfDifficulty);
            final int _tmpInterval;
            _tmpInterval = _cursor.getInt(_cursorIndexOfInterval);
            final int _tmpRepetitions;
            _tmpRepetitions = _cursor.getInt(_cursorIndexOfRepetitions);
            final float _tmpEaseFactor;
            _tmpEaseFactor = _cursor.getFloat(_cursorIndexOfEaseFactor);
            final int _tmpLapses;
            _tmpLapses = _cursor.getInt(_cursorIndexOfLapses);
            final long _tmpDueDate;
            _tmpDueDate = _cursor.getLong(_cursorIndexOfDueDate);
            final Long _tmpLastReviewedDate;
            if (_cursor.isNull(_cursorIndexOfLastReviewedDate)) {
              _tmpLastReviewedDate = null;
            } else {
              _tmpLastReviewedDate = _cursor.getLong(_cursorIndexOfLastReviewedDate);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new CardEntity(_tmpId,_tmpDeckId,_tmpType,_tmpPrompt,_tmpAnswer,_tmpExplanation,_tmpLatexFormula,_tmpClozeOcclusion,_tmpDerivationStepsJson,_tmpKeyTermsJson,_tmpMisconceptionsJson,_tmpDifficulty,_tmpInterval,_tmpRepetitions,_tmpEaseFactor,_tmpLapses,_tmpDueDate,_tmpLastReviewedDate,_tmpCreatedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<CardEntity>> getDueCardsForDeck(final long deckId, final long currentTimestamp) {
    final String _sql = "SELECT * FROM cards WHERE deckId = ? AND dueDate <= ? ORDER BY dueDate ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, deckId);
    _argIndex = 2;
    _statement.bindLong(_argIndex, currentTimestamp);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"cards"}, new Callable<List<CardEntity>>() {
      @Override
      @NonNull
      public List<CardEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDeckId = CursorUtil.getColumnIndexOrThrow(_cursor, "deckId");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfPrompt = CursorUtil.getColumnIndexOrThrow(_cursor, "prompt");
          final int _cursorIndexOfAnswer = CursorUtil.getColumnIndexOrThrow(_cursor, "answer");
          final int _cursorIndexOfExplanation = CursorUtil.getColumnIndexOrThrow(_cursor, "explanation");
          final int _cursorIndexOfLatexFormula = CursorUtil.getColumnIndexOrThrow(_cursor, "latexFormula");
          final int _cursorIndexOfClozeOcclusion = CursorUtil.getColumnIndexOrThrow(_cursor, "clozeOcclusion");
          final int _cursorIndexOfDerivationStepsJson = CursorUtil.getColumnIndexOrThrow(_cursor, "derivationStepsJson");
          final int _cursorIndexOfKeyTermsJson = CursorUtil.getColumnIndexOrThrow(_cursor, "keyTermsJson");
          final int _cursorIndexOfMisconceptionsJson = CursorUtil.getColumnIndexOrThrow(_cursor, "misconceptionsJson");
          final int _cursorIndexOfDifficulty = CursorUtil.getColumnIndexOrThrow(_cursor, "difficulty");
          final int _cursorIndexOfInterval = CursorUtil.getColumnIndexOrThrow(_cursor, "interval");
          final int _cursorIndexOfRepetitions = CursorUtil.getColumnIndexOrThrow(_cursor, "repetitions");
          final int _cursorIndexOfEaseFactor = CursorUtil.getColumnIndexOrThrow(_cursor, "easeFactor");
          final int _cursorIndexOfLapses = CursorUtil.getColumnIndexOrThrow(_cursor, "lapses");
          final int _cursorIndexOfDueDate = CursorUtil.getColumnIndexOrThrow(_cursor, "dueDate");
          final int _cursorIndexOfLastReviewedDate = CursorUtil.getColumnIndexOrThrow(_cursor, "lastReviewedDate");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<CardEntity> _result = new ArrayList<CardEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final CardEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpDeckId;
            _tmpDeckId = _cursor.getLong(_cursorIndexOfDeckId);
            final CardType _tmpType;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfType);
            _tmpType = __converters.toCardType(_tmp);
            final String _tmpPrompt;
            _tmpPrompt = _cursor.getString(_cursorIndexOfPrompt);
            final String _tmpAnswer;
            _tmpAnswer = _cursor.getString(_cursorIndexOfAnswer);
            final String _tmpExplanation;
            _tmpExplanation = _cursor.getString(_cursorIndexOfExplanation);
            final String _tmpLatexFormula;
            if (_cursor.isNull(_cursorIndexOfLatexFormula)) {
              _tmpLatexFormula = null;
            } else {
              _tmpLatexFormula = _cursor.getString(_cursorIndexOfLatexFormula);
            }
            final String _tmpClozeOcclusion;
            if (_cursor.isNull(_cursorIndexOfClozeOcclusion)) {
              _tmpClozeOcclusion = null;
            } else {
              _tmpClozeOcclusion = _cursor.getString(_cursorIndexOfClozeOcclusion);
            }
            final String _tmpDerivationStepsJson;
            if (_cursor.isNull(_cursorIndexOfDerivationStepsJson)) {
              _tmpDerivationStepsJson = null;
            } else {
              _tmpDerivationStepsJson = _cursor.getString(_cursorIndexOfDerivationStepsJson);
            }
            final String _tmpKeyTermsJson;
            if (_cursor.isNull(_cursorIndexOfKeyTermsJson)) {
              _tmpKeyTermsJson = null;
            } else {
              _tmpKeyTermsJson = _cursor.getString(_cursorIndexOfKeyTermsJson);
            }
            final String _tmpMisconceptionsJson;
            if (_cursor.isNull(_cursorIndexOfMisconceptionsJson)) {
              _tmpMisconceptionsJson = null;
            } else {
              _tmpMisconceptionsJson = _cursor.getString(_cursorIndexOfMisconceptionsJson);
            }
            final String _tmpDifficulty;
            _tmpDifficulty = _cursor.getString(_cursorIndexOfDifficulty);
            final int _tmpInterval;
            _tmpInterval = _cursor.getInt(_cursorIndexOfInterval);
            final int _tmpRepetitions;
            _tmpRepetitions = _cursor.getInt(_cursorIndexOfRepetitions);
            final float _tmpEaseFactor;
            _tmpEaseFactor = _cursor.getFloat(_cursorIndexOfEaseFactor);
            final int _tmpLapses;
            _tmpLapses = _cursor.getInt(_cursorIndexOfLapses);
            final long _tmpDueDate;
            _tmpDueDate = _cursor.getLong(_cursorIndexOfDueDate);
            final Long _tmpLastReviewedDate;
            if (_cursor.isNull(_cursorIndexOfLastReviewedDate)) {
              _tmpLastReviewedDate = null;
            } else {
              _tmpLastReviewedDate = _cursor.getLong(_cursorIndexOfLastReviewedDate);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new CardEntity(_tmpId,_tmpDeckId,_tmpType,_tmpPrompt,_tmpAnswer,_tmpExplanation,_tmpLatexFormula,_tmpClozeOcclusion,_tmpDerivationStepsJson,_tmpKeyTermsJson,_tmpMisconceptionsJson,_tmpDifficulty,_tmpInterval,_tmpRepetitions,_tmpEaseFactor,_tmpLapses,_tmpDueDate,_tmpLastReviewedDate,_tmpCreatedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getDueCardsBatch(final long currentTimestamp, final int limit,
      final Continuation<? super List<CardEntity>> $completion) {
    final String _sql = "SELECT * FROM cards WHERE dueDate <= ? ORDER BY dueDate ASC LIMIT ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, currentTimestamp);
    _argIndex = 2;
    _statement.bindLong(_argIndex, limit);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<CardEntity>>() {
      @Override
      @NonNull
      public List<CardEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDeckId = CursorUtil.getColumnIndexOrThrow(_cursor, "deckId");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfPrompt = CursorUtil.getColumnIndexOrThrow(_cursor, "prompt");
          final int _cursorIndexOfAnswer = CursorUtil.getColumnIndexOrThrow(_cursor, "answer");
          final int _cursorIndexOfExplanation = CursorUtil.getColumnIndexOrThrow(_cursor, "explanation");
          final int _cursorIndexOfLatexFormula = CursorUtil.getColumnIndexOrThrow(_cursor, "latexFormula");
          final int _cursorIndexOfClozeOcclusion = CursorUtil.getColumnIndexOrThrow(_cursor, "clozeOcclusion");
          final int _cursorIndexOfDerivationStepsJson = CursorUtil.getColumnIndexOrThrow(_cursor, "derivationStepsJson");
          final int _cursorIndexOfKeyTermsJson = CursorUtil.getColumnIndexOrThrow(_cursor, "keyTermsJson");
          final int _cursorIndexOfMisconceptionsJson = CursorUtil.getColumnIndexOrThrow(_cursor, "misconceptionsJson");
          final int _cursorIndexOfDifficulty = CursorUtil.getColumnIndexOrThrow(_cursor, "difficulty");
          final int _cursorIndexOfInterval = CursorUtil.getColumnIndexOrThrow(_cursor, "interval");
          final int _cursorIndexOfRepetitions = CursorUtil.getColumnIndexOrThrow(_cursor, "repetitions");
          final int _cursorIndexOfEaseFactor = CursorUtil.getColumnIndexOrThrow(_cursor, "easeFactor");
          final int _cursorIndexOfLapses = CursorUtil.getColumnIndexOrThrow(_cursor, "lapses");
          final int _cursorIndexOfDueDate = CursorUtil.getColumnIndexOrThrow(_cursor, "dueDate");
          final int _cursorIndexOfLastReviewedDate = CursorUtil.getColumnIndexOrThrow(_cursor, "lastReviewedDate");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<CardEntity> _result = new ArrayList<CardEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final CardEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpDeckId;
            _tmpDeckId = _cursor.getLong(_cursorIndexOfDeckId);
            final CardType _tmpType;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfType);
            _tmpType = __converters.toCardType(_tmp);
            final String _tmpPrompt;
            _tmpPrompt = _cursor.getString(_cursorIndexOfPrompt);
            final String _tmpAnswer;
            _tmpAnswer = _cursor.getString(_cursorIndexOfAnswer);
            final String _tmpExplanation;
            _tmpExplanation = _cursor.getString(_cursorIndexOfExplanation);
            final String _tmpLatexFormula;
            if (_cursor.isNull(_cursorIndexOfLatexFormula)) {
              _tmpLatexFormula = null;
            } else {
              _tmpLatexFormula = _cursor.getString(_cursorIndexOfLatexFormula);
            }
            final String _tmpClozeOcclusion;
            if (_cursor.isNull(_cursorIndexOfClozeOcclusion)) {
              _tmpClozeOcclusion = null;
            } else {
              _tmpClozeOcclusion = _cursor.getString(_cursorIndexOfClozeOcclusion);
            }
            final String _tmpDerivationStepsJson;
            if (_cursor.isNull(_cursorIndexOfDerivationStepsJson)) {
              _tmpDerivationStepsJson = null;
            } else {
              _tmpDerivationStepsJson = _cursor.getString(_cursorIndexOfDerivationStepsJson);
            }
            final String _tmpKeyTermsJson;
            if (_cursor.isNull(_cursorIndexOfKeyTermsJson)) {
              _tmpKeyTermsJson = null;
            } else {
              _tmpKeyTermsJson = _cursor.getString(_cursorIndexOfKeyTermsJson);
            }
            final String _tmpMisconceptionsJson;
            if (_cursor.isNull(_cursorIndexOfMisconceptionsJson)) {
              _tmpMisconceptionsJson = null;
            } else {
              _tmpMisconceptionsJson = _cursor.getString(_cursorIndexOfMisconceptionsJson);
            }
            final String _tmpDifficulty;
            _tmpDifficulty = _cursor.getString(_cursorIndexOfDifficulty);
            final int _tmpInterval;
            _tmpInterval = _cursor.getInt(_cursorIndexOfInterval);
            final int _tmpRepetitions;
            _tmpRepetitions = _cursor.getInt(_cursorIndexOfRepetitions);
            final float _tmpEaseFactor;
            _tmpEaseFactor = _cursor.getFloat(_cursorIndexOfEaseFactor);
            final int _tmpLapses;
            _tmpLapses = _cursor.getInt(_cursorIndexOfLapses);
            final long _tmpDueDate;
            _tmpDueDate = _cursor.getLong(_cursorIndexOfDueDate);
            final Long _tmpLastReviewedDate;
            if (_cursor.isNull(_cursorIndexOfLastReviewedDate)) {
              _tmpLastReviewedDate = null;
            } else {
              _tmpLastReviewedDate = _cursor.getLong(_cursorIndexOfLastReviewedDate);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new CardEntity(_tmpId,_tmpDeckId,_tmpType,_tmpPrompt,_tmpAnswer,_tmpExplanation,_tmpLatexFormula,_tmpClozeOcclusion,_tmpDerivationStepsJson,_tmpKeyTermsJson,_tmpMisconceptionsJson,_tmpDifficulty,_tmpInterval,_tmpRepetitions,_tmpEaseFactor,_tmpLapses,_tmpDueDate,_tmpLastReviewedDate,_tmpCreatedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getDueCardsForDeckBatch(final long deckId, final long currentTimestamp,
      final int limit, final Continuation<? super List<CardEntity>> $completion) {
    final String _sql = "SELECT * FROM cards WHERE deckId = ? AND dueDate <= ? ORDER BY dueDate ASC LIMIT ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 3);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, deckId);
    _argIndex = 2;
    _statement.bindLong(_argIndex, currentTimestamp);
    _argIndex = 3;
    _statement.bindLong(_argIndex, limit);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<CardEntity>>() {
      @Override
      @NonNull
      public List<CardEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDeckId = CursorUtil.getColumnIndexOrThrow(_cursor, "deckId");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfPrompt = CursorUtil.getColumnIndexOrThrow(_cursor, "prompt");
          final int _cursorIndexOfAnswer = CursorUtil.getColumnIndexOrThrow(_cursor, "answer");
          final int _cursorIndexOfExplanation = CursorUtil.getColumnIndexOrThrow(_cursor, "explanation");
          final int _cursorIndexOfLatexFormula = CursorUtil.getColumnIndexOrThrow(_cursor, "latexFormula");
          final int _cursorIndexOfClozeOcclusion = CursorUtil.getColumnIndexOrThrow(_cursor, "clozeOcclusion");
          final int _cursorIndexOfDerivationStepsJson = CursorUtil.getColumnIndexOrThrow(_cursor, "derivationStepsJson");
          final int _cursorIndexOfKeyTermsJson = CursorUtil.getColumnIndexOrThrow(_cursor, "keyTermsJson");
          final int _cursorIndexOfMisconceptionsJson = CursorUtil.getColumnIndexOrThrow(_cursor, "misconceptionsJson");
          final int _cursorIndexOfDifficulty = CursorUtil.getColumnIndexOrThrow(_cursor, "difficulty");
          final int _cursorIndexOfInterval = CursorUtil.getColumnIndexOrThrow(_cursor, "interval");
          final int _cursorIndexOfRepetitions = CursorUtil.getColumnIndexOrThrow(_cursor, "repetitions");
          final int _cursorIndexOfEaseFactor = CursorUtil.getColumnIndexOrThrow(_cursor, "easeFactor");
          final int _cursorIndexOfLapses = CursorUtil.getColumnIndexOrThrow(_cursor, "lapses");
          final int _cursorIndexOfDueDate = CursorUtil.getColumnIndexOrThrow(_cursor, "dueDate");
          final int _cursorIndexOfLastReviewedDate = CursorUtil.getColumnIndexOrThrow(_cursor, "lastReviewedDate");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<CardEntity> _result = new ArrayList<CardEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final CardEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpDeckId;
            _tmpDeckId = _cursor.getLong(_cursorIndexOfDeckId);
            final CardType _tmpType;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfType);
            _tmpType = __converters.toCardType(_tmp);
            final String _tmpPrompt;
            _tmpPrompt = _cursor.getString(_cursorIndexOfPrompt);
            final String _tmpAnswer;
            _tmpAnswer = _cursor.getString(_cursorIndexOfAnswer);
            final String _tmpExplanation;
            _tmpExplanation = _cursor.getString(_cursorIndexOfExplanation);
            final String _tmpLatexFormula;
            if (_cursor.isNull(_cursorIndexOfLatexFormula)) {
              _tmpLatexFormula = null;
            } else {
              _tmpLatexFormula = _cursor.getString(_cursorIndexOfLatexFormula);
            }
            final String _tmpClozeOcclusion;
            if (_cursor.isNull(_cursorIndexOfClozeOcclusion)) {
              _tmpClozeOcclusion = null;
            } else {
              _tmpClozeOcclusion = _cursor.getString(_cursorIndexOfClozeOcclusion);
            }
            final String _tmpDerivationStepsJson;
            if (_cursor.isNull(_cursorIndexOfDerivationStepsJson)) {
              _tmpDerivationStepsJson = null;
            } else {
              _tmpDerivationStepsJson = _cursor.getString(_cursorIndexOfDerivationStepsJson);
            }
            final String _tmpKeyTermsJson;
            if (_cursor.isNull(_cursorIndexOfKeyTermsJson)) {
              _tmpKeyTermsJson = null;
            } else {
              _tmpKeyTermsJson = _cursor.getString(_cursorIndexOfKeyTermsJson);
            }
            final String _tmpMisconceptionsJson;
            if (_cursor.isNull(_cursorIndexOfMisconceptionsJson)) {
              _tmpMisconceptionsJson = null;
            } else {
              _tmpMisconceptionsJson = _cursor.getString(_cursorIndexOfMisconceptionsJson);
            }
            final String _tmpDifficulty;
            _tmpDifficulty = _cursor.getString(_cursorIndexOfDifficulty);
            final int _tmpInterval;
            _tmpInterval = _cursor.getInt(_cursorIndexOfInterval);
            final int _tmpRepetitions;
            _tmpRepetitions = _cursor.getInt(_cursorIndexOfRepetitions);
            final float _tmpEaseFactor;
            _tmpEaseFactor = _cursor.getFloat(_cursorIndexOfEaseFactor);
            final int _tmpLapses;
            _tmpLapses = _cursor.getInt(_cursorIndexOfLapses);
            final long _tmpDueDate;
            _tmpDueDate = _cursor.getLong(_cursorIndexOfDueDate);
            final Long _tmpLastReviewedDate;
            if (_cursor.isNull(_cursorIndexOfLastReviewedDate)) {
              _tmpLastReviewedDate = null;
            } else {
              _tmpLastReviewedDate = _cursor.getLong(_cursorIndexOfLastReviewedDate);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new CardEntity(_tmpId,_tmpDeckId,_tmpType,_tmpPrompt,_tmpAnswer,_tmpExplanation,_tmpLatexFormula,_tmpClozeOcclusion,_tmpDerivationStepsJson,_tmpKeyTermsJson,_tmpMisconceptionsJson,_tmpDifficulty,_tmpInterval,_tmpRepetitions,_tmpEaseFactor,_tmpLapses,_tmpDueDate,_tmpLastReviewedDate,_tmpCreatedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<Integer> countDueCards(final long currentTimestamp) {
    final String _sql = "SELECT COUNT(*) FROM cards WHERE dueDate <= ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, currentTimestamp);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"cards"}, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<RetentionOverview> getRetentionOverview(final long currentTimestamp) {
    final String _sql = "\n"
            + "        SELECT \n"
            + "            COUNT(*) as totalCards,\n"
            + "            SUM(CASE WHEN dueDate <= ? THEN 1 ELSE 0 END) as dueCards,\n"
            + "            SUM(CASE WHEN `interval` = 0 THEN 1 ELSE 0 END) as learningCards,\n"
            + "            SUM(CASE WHEN `interval` > 0 AND `interval` < 21 THEN 1 ELSE 0 END) as youngCards,\n"
            + "            SUM(CASE WHEN `interval` >= 21 THEN 1 ELSE 0 END) as matureCards\n"
            + "        FROM cards\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, currentTimestamp);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"cards"}, new Callable<RetentionOverview>() {
      @Override
      @NonNull
      public RetentionOverview call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfTotalCards = 0;
          final int _cursorIndexOfDueCards = 1;
          final int _cursorIndexOfLearningCards = 2;
          final int _cursorIndexOfYoungCards = 3;
          final int _cursorIndexOfMatureCards = 4;
          final RetentionOverview _result;
          if (_cursor.moveToFirst()) {
            final int _tmpTotalCards;
            _tmpTotalCards = _cursor.getInt(_cursorIndexOfTotalCards);
            final int _tmpDueCards;
            _tmpDueCards = _cursor.getInt(_cursorIndexOfDueCards);
            final int _tmpLearningCards;
            _tmpLearningCards = _cursor.getInt(_cursorIndexOfLearningCards);
            final int _tmpYoungCards;
            _tmpYoungCards = _cursor.getInt(_cursorIndexOfYoungCards);
            final int _tmpMatureCards;
            _tmpMatureCards = _cursor.getInt(_cursorIndexOfMatureCards);
            _result = new RetentionOverview(_tmpTotalCards,_tmpDueCards,_tmpLearningCards,_tmpYoungCards,_tmpMatureCards);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getAllCardsList(final Continuation<? super List<CardEntity>> $completion) {
    final String _sql = "SELECT * FROM cards ORDER BY createdAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<CardEntity>>() {
      @Override
      @NonNull
      public List<CardEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDeckId = CursorUtil.getColumnIndexOrThrow(_cursor, "deckId");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfPrompt = CursorUtil.getColumnIndexOrThrow(_cursor, "prompt");
          final int _cursorIndexOfAnswer = CursorUtil.getColumnIndexOrThrow(_cursor, "answer");
          final int _cursorIndexOfExplanation = CursorUtil.getColumnIndexOrThrow(_cursor, "explanation");
          final int _cursorIndexOfLatexFormula = CursorUtil.getColumnIndexOrThrow(_cursor, "latexFormula");
          final int _cursorIndexOfClozeOcclusion = CursorUtil.getColumnIndexOrThrow(_cursor, "clozeOcclusion");
          final int _cursorIndexOfDerivationStepsJson = CursorUtil.getColumnIndexOrThrow(_cursor, "derivationStepsJson");
          final int _cursorIndexOfKeyTermsJson = CursorUtil.getColumnIndexOrThrow(_cursor, "keyTermsJson");
          final int _cursorIndexOfMisconceptionsJson = CursorUtil.getColumnIndexOrThrow(_cursor, "misconceptionsJson");
          final int _cursorIndexOfDifficulty = CursorUtil.getColumnIndexOrThrow(_cursor, "difficulty");
          final int _cursorIndexOfInterval = CursorUtil.getColumnIndexOrThrow(_cursor, "interval");
          final int _cursorIndexOfRepetitions = CursorUtil.getColumnIndexOrThrow(_cursor, "repetitions");
          final int _cursorIndexOfEaseFactor = CursorUtil.getColumnIndexOrThrow(_cursor, "easeFactor");
          final int _cursorIndexOfLapses = CursorUtil.getColumnIndexOrThrow(_cursor, "lapses");
          final int _cursorIndexOfDueDate = CursorUtil.getColumnIndexOrThrow(_cursor, "dueDate");
          final int _cursorIndexOfLastReviewedDate = CursorUtil.getColumnIndexOrThrow(_cursor, "lastReviewedDate");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<CardEntity> _result = new ArrayList<CardEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final CardEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpDeckId;
            _tmpDeckId = _cursor.getLong(_cursorIndexOfDeckId);
            final CardType _tmpType;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfType);
            _tmpType = __converters.toCardType(_tmp);
            final String _tmpPrompt;
            _tmpPrompt = _cursor.getString(_cursorIndexOfPrompt);
            final String _tmpAnswer;
            _tmpAnswer = _cursor.getString(_cursorIndexOfAnswer);
            final String _tmpExplanation;
            _tmpExplanation = _cursor.getString(_cursorIndexOfExplanation);
            final String _tmpLatexFormula;
            if (_cursor.isNull(_cursorIndexOfLatexFormula)) {
              _tmpLatexFormula = null;
            } else {
              _tmpLatexFormula = _cursor.getString(_cursorIndexOfLatexFormula);
            }
            final String _tmpClozeOcclusion;
            if (_cursor.isNull(_cursorIndexOfClozeOcclusion)) {
              _tmpClozeOcclusion = null;
            } else {
              _tmpClozeOcclusion = _cursor.getString(_cursorIndexOfClozeOcclusion);
            }
            final String _tmpDerivationStepsJson;
            if (_cursor.isNull(_cursorIndexOfDerivationStepsJson)) {
              _tmpDerivationStepsJson = null;
            } else {
              _tmpDerivationStepsJson = _cursor.getString(_cursorIndexOfDerivationStepsJson);
            }
            final String _tmpKeyTermsJson;
            if (_cursor.isNull(_cursorIndexOfKeyTermsJson)) {
              _tmpKeyTermsJson = null;
            } else {
              _tmpKeyTermsJson = _cursor.getString(_cursorIndexOfKeyTermsJson);
            }
            final String _tmpMisconceptionsJson;
            if (_cursor.isNull(_cursorIndexOfMisconceptionsJson)) {
              _tmpMisconceptionsJson = null;
            } else {
              _tmpMisconceptionsJson = _cursor.getString(_cursorIndexOfMisconceptionsJson);
            }
            final String _tmpDifficulty;
            _tmpDifficulty = _cursor.getString(_cursorIndexOfDifficulty);
            final int _tmpInterval;
            _tmpInterval = _cursor.getInt(_cursorIndexOfInterval);
            final int _tmpRepetitions;
            _tmpRepetitions = _cursor.getInt(_cursorIndexOfRepetitions);
            final float _tmpEaseFactor;
            _tmpEaseFactor = _cursor.getFloat(_cursorIndexOfEaseFactor);
            final int _tmpLapses;
            _tmpLapses = _cursor.getInt(_cursorIndexOfLapses);
            final long _tmpDueDate;
            _tmpDueDate = _cursor.getLong(_cursorIndexOfDueDate);
            final Long _tmpLastReviewedDate;
            if (_cursor.isNull(_cursorIndexOfLastReviewedDate)) {
              _tmpLastReviewedDate = null;
            } else {
              _tmpLastReviewedDate = _cursor.getLong(_cursorIndexOfLastReviewedDate);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new CardEntity(_tmpId,_tmpDeckId,_tmpType,_tmpPrompt,_tmpAnswer,_tmpExplanation,_tmpLatexFormula,_tmpClozeOcclusion,_tmpDerivationStepsJson,_tmpKeyTermsJson,_tmpMisconceptionsJson,_tmpDifficulty,_tmpInterval,_tmpRepetitions,_tmpEaseFactor,_tmpLapses,_tmpDueDate,_tmpLastReviewedDate,_tmpCreatedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}

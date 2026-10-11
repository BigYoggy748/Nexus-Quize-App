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
import com.nexus.quiz.data.local.entity.DeckEntity;
import com.nexus.quiz.data.model.DeckCategory;
import java.lang.Class;
import java.lang.Exception;
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
public final class DeckDao_Impl implements DeckDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<DeckEntity> __insertionAdapterOfDeckEntity;

  private final Converters __converters = new Converters();

  private final EntityDeletionOrUpdateAdapter<DeckEntity> __deletionAdapterOfDeckEntity;

  private final EntityDeletionOrUpdateAdapter<DeckEntity> __updateAdapterOfDeckEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteDeckById;

  public DeckDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfDeckEntity = new EntityInsertionAdapter<DeckEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `decks` (`id`,`name`,`description`,`category`,`hexColor`,`createdAt`,`updatedAt`) VALUES (nullif(?, 0),?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final DeckEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        statement.bindString(3, entity.getDescription());
        final String _tmp = __converters.fromDeckCategory(entity.getCategory());
        statement.bindString(4, _tmp);
        statement.bindString(5, entity.getHexColor());
        statement.bindLong(6, entity.getCreatedAt());
        statement.bindLong(7, entity.getUpdatedAt());
      }
    };
    this.__deletionAdapterOfDeckEntity = new EntityDeletionOrUpdateAdapter<DeckEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `decks` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final DeckEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfDeckEntity = new EntityDeletionOrUpdateAdapter<DeckEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `decks` SET `id` = ?,`name` = ?,`description` = ?,`category` = ?,`hexColor` = ?,`createdAt` = ?,`updatedAt` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final DeckEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        statement.bindString(3, entity.getDescription());
        final String _tmp = __converters.fromDeckCategory(entity.getCategory());
        statement.bindString(4, _tmp);
        statement.bindString(5, entity.getHexColor());
        statement.bindLong(6, entity.getCreatedAt());
        statement.bindLong(7, entity.getUpdatedAt());
        statement.bindLong(8, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteDeckById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM decks WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertDeck(final DeckEntity deck, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfDeckEntity.insertAndReturnId(deck);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteDeck(final DeckEntity deck, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfDeckEntity.handle(deck);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateDeck(final DeckEntity deck, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfDeckEntity.handle(deck);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteDeckById(final long deckId, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteDeckById.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, deckId);
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
          __preparedStmtOfDeleteDeckById.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<DeckEntity>> getAllDecks() {
    final String _sql = "SELECT * FROM decks ORDER BY updatedAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"decks"}, new Callable<List<DeckEntity>>() {
      @Override
      @NonNull
      public List<DeckEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfHexColor = CursorUtil.getColumnIndexOrThrow(_cursor, "hexColor");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final List<DeckEntity> _result = new ArrayList<DeckEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final DeckEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpDescription;
            _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            final DeckCategory _tmpCategory;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfCategory);
            _tmpCategory = __converters.toDeckCategory(_tmp);
            final String _tmpHexColor;
            _tmpHexColor = _cursor.getString(_cursorIndexOfHexColor);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new DeckEntity(_tmpId,_tmpName,_tmpDescription,_tmpCategory,_tmpHexColor,_tmpCreatedAt,_tmpUpdatedAt);
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
  public Object getDeckById(final long deckId, final Continuation<? super DeckEntity> $completion) {
    final String _sql = "SELECT * FROM decks WHERE id = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, deckId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<DeckEntity>() {
      @Override
      @Nullable
      public DeckEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfHexColor = CursorUtil.getColumnIndexOrThrow(_cursor, "hexColor");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final DeckEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpDescription;
            _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            final DeckCategory _tmpCategory;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfCategory);
            _tmpCategory = __converters.toDeckCategory(_tmp);
            final String _tmpHexColor;
            _tmpHexColor = _cursor.getString(_cursorIndexOfHexColor);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _result = new DeckEntity(_tmpId,_tmpName,_tmpDescription,_tmpCategory,_tmpHexColor,_tmpCreatedAt,_tmpUpdatedAt);
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
  public Flow<DeckEntity> getDeckByIdFlow(final long deckId) {
    final String _sql = "SELECT * FROM decks WHERE id = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, deckId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"decks"}, new Callable<DeckEntity>() {
      @Override
      @Nullable
      public DeckEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfHexColor = CursorUtil.getColumnIndexOrThrow(_cursor, "hexColor");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final DeckEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpDescription;
            _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            final DeckCategory _tmpCategory;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfCategory);
            _tmpCategory = __converters.toDeckCategory(_tmp);
            final String _tmpHexColor;
            _tmpHexColor = _cursor.getString(_cursorIndexOfHexColor);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _result = new DeckEntity(_tmpId,_tmpName,_tmpDescription,_tmpCategory,_tmpHexColor,_tmpCreatedAt,_tmpUpdatedAt);
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
  public Flow<List<DeckWithStats>> getDecksWithStats(final long currentTimestamp) {
    final String _sql = "\n"
            + "        SELECT \n"
            + "            d.id,\n"
            + "            d.name,\n"
            + "            d.description,\n"
            + "            d.category,\n"
            + "            d.hexColor,\n"
            + "            d.createdAt,\n"
            + "            d.updatedAt,\n"
            + "            COUNT(c.id) as totalCards,\n"
            + "            SUM(CASE WHEN c.dueDate <= ? THEN 1 ELSE 0 END) as dueCards,\n"
            + "            SUM(CASE WHEN c.interval >= 21 THEN 1 ELSE 0 END) as masteredCards\n"
            + "        FROM decks d\n"
            + "        LEFT JOIN cards c ON d.id = c.deckId\n"
            + "        GROUP BY d.id\n"
            + "        ORDER BY d.updatedAt DESC\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, currentTimestamp);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"decks",
        "cards"}, new Callable<List<DeckWithStats>>() {
      @Override
      @NonNull
      public List<DeckWithStats> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = 0;
          final int _cursorIndexOfName = 1;
          final int _cursorIndexOfDescription = 2;
          final int _cursorIndexOfCategory = 3;
          final int _cursorIndexOfHexColor = 4;
          final int _cursorIndexOfCreatedAt = 5;
          final int _cursorIndexOfUpdatedAt = 6;
          final int _cursorIndexOfTotalCards = 7;
          final int _cursorIndexOfDueCards = 8;
          final int _cursorIndexOfMasteredCards = 9;
          final List<DeckWithStats> _result = new ArrayList<DeckWithStats>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final DeckWithStats _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpDescription;
            _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            final DeckCategory _tmpCategory;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfCategory);
            _tmpCategory = __converters.toDeckCategory(_tmp);
            final String _tmpHexColor;
            _tmpHexColor = _cursor.getString(_cursorIndexOfHexColor);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            final int _tmpTotalCards;
            _tmpTotalCards = _cursor.getInt(_cursorIndexOfTotalCards);
            final int _tmpDueCards;
            _tmpDueCards = _cursor.getInt(_cursorIndexOfDueCards);
            final int _tmpMasteredCards;
            _tmpMasteredCards = _cursor.getInt(_cursorIndexOfMasteredCards);
            _item = new DeckWithStats(_tmpId,_tmpName,_tmpDescription,_tmpCategory,_tmpHexColor,_tmpCreatedAt,_tmpUpdatedAt,_tmpTotalCards,_tmpDueCards,_tmpMasteredCards);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}

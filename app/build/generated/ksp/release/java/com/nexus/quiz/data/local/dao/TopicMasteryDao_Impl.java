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
import com.nexus.quiz.data.local.entity.TopicMasteryEntity;
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
public final class TopicMasteryDao_Impl implements TopicMasteryDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<TopicMasteryEntity> __insertionAdapterOfTopicMasteryEntity;

  private final EntityDeletionOrUpdateAdapter<TopicMasteryEntity> __deletionAdapterOfTopicMasteryEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteTopicById;

  public TopicMasteryDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfTopicMasteryEntity = new EntityInsertionAdapter<TopicMasteryEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `saved_topics` (`id`,`topicTitle`,`topicHint`,`rawNotes`,`jsonContent`,`cardCount`,`createdAt`) VALUES (nullif(?, 0),?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final TopicMasteryEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getTopicTitle());
        statement.bindString(3, entity.getTopicHint());
        statement.bindString(4, entity.getRawNotes());
        statement.bindString(5, entity.getJsonContent());
        statement.bindLong(6, entity.getCardCount());
        statement.bindLong(7, entity.getCreatedAt());
      }
    };
    this.__deletionAdapterOfTopicMasteryEntity = new EntityDeletionOrUpdateAdapter<TopicMasteryEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `saved_topics` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final TopicMasteryEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteTopicById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM saved_topics WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertTopic(final TopicMasteryEntity topic,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfTopicMasteryEntity.insertAndReturnId(topic);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteTopic(final TopicMasteryEntity topic,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfTopicMasteryEntity.handle(topic);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteTopicById(final long id, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteTopicById.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, id);
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
          __preparedStmtOfDeleteTopicById.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<TopicMasteryEntity>> getAllSavedTopics() {
    final String _sql = "SELECT * FROM saved_topics ORDER BY createdAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"saved_topics"}, new Callable<List<TopicMasteryEntity>>() {
      @Override
      @NonNull
      public List<TopicMasteryEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTopicTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "topicTitle");
          final int _cursorIndexOfTopicHint = CursorUtil.getColumnIndexOrThrow(_cursor, "topicHint");
          final int _cursorIndexOfRawNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "rawNotes");
          final int _cursorIndexOfJsonContent = CursorUtil.getColumnIndexOrThrow(_cursor, "jsonContent");
          final int _cursorIndexOfCardCount = CursorUtil.getColumnIndexOrThrow(_cursor, "cardCount");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<TopicMasteryEntity> _result = new ArrayList<TopicMasteryEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final TopicMasteryEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTopicTitle;
            _tmpTopicTitle = _cursor.getString(_cursorIndexOfTopicTitle);
            final String _tmpTopicHint;
            _tmpTopicHint = _cursor.getString(_cursorIndexOfTopicHint);
            final String _tmpRawNotes;
            _tmpRawNotes = _cursor.getString(_cursorIndexOfRawNotes);
            final String _tmpJsonContent;
            _tmpJsonContent = _cursor.getString(_cursorIndexOfJsonContent);
            final int _tmpCardCount;
            _tmpCardCount = _cursor.getInt(_cursorIndexOfCardCount);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new TopicMasteryEntity(_tmpId,_tmpTopicTitle,_tmpTopicHint,_tmpRawNotes,_tmpJsonContent,_tmpCardCount,_tmpCreatedAt);
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
  public Object getTopicById(final long id,
      final Continuation<? super TopicMasteryEntity> $completion) {
    final String _sql = "SELECT * FROM saved_topics WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<TopicMasteryEntity>() {
      @Override
      @Nullable
      public TopicMasteryEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTopicTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "topicTitle");
          final int _cursorIndexOfTopicHint = CursorUtil.getColumnIndexOrThrow(_cursor, "topicHint");
          final int _cursorIndexOfRawNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "rawNotes");
          final int _cursorIndexOfJsonContent = CursorUtil.getColumnIndexOrThrow(_cursor, "jsonContent");
          final int _cursorIndexOfCardCount = CursorUtil.getColumnIndexOrThrow(_cursor, "cardCount");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final TopicMasteryEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTopicTitle;
            _tmpTopicTitle = _cursor.getString(_cursorIndexOfTopicTitle);
            final String _tmpTopicHint;
            _tmpTopicHint = _cursor.getString(_cursorIndexOfTopicHint);
            final String _tmpRawNotes;
            _tmpRawNotes = _cursor.getString(_cursorIndexOfRawNotes);
            final String _tmpJsonContent;
            _tmpJsonContent = _cursor.getString(_cursorIndexOfJsonContent);
            final int _tmpCardCount;
            _tmpCardCount = _cursor.getInt(_cursorIndexOfCardCount);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _result = new TopicMasteryEntity(_tmpId,_tmpTopicTitle,_tmpTopicHint,_tmpRawNotes,_tmpJsonContent,_tmpCardCount,_tmpCreatedAt);
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
  public Object getTopicByTitle(final String title,
      final Continuation<? super TopicMasteryEntity> $completion) {
    final String _sql = "SELECT * FROM saved_topics WHERE topicTitle = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, title);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<TopicMasteryEntity>() {
      @Override
      @Nullable
      public TopicMasteryEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTopicTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "topicTitle");
          final int _cursorIndexOfTopicHint = CursorUtil.getColumnIndexOrThrow(_cursor, "topicHint");
          final int _cursorIndexOfRawNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "rawNotes");
          final int _cursorIndexOfJsonContent = CursorUtil.getColumnIndexOrThrow(_cursor, "jsonContent");
          final int _cursorIndexOfCardCount = CursorUtil.getColumnIndexOrThrow(_cursor, "cardCount");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final TopicMasteryEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTopicTitle;
            _tmpTopicTitle = _cursor.getString(_cursorIndexOfTopicTitle);
            final String _tmpTopicHint;
            _tmpTopicHint = _cursor.getString(_cursorIndexOfTopicHint);
            final String _tmpRawNotes;
            _tmpRawNotes = _cursor.getString(_cursorIndexOfRawNotes);
            final String _tmpJsonContent;
            _tmpJsonContent = _cursor.getString(_cursorIndexOfJsonContent);
            final int _tmpCardCount;
            _tmpCardCount = _cursor.getInt(_cursorIndexOfCardCount);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _result = new TopicMasteryEntity(_tmpId,_tmpTopicTitle,_tmpTopicHint,_tmpRawNotes,_tmpJsonContent,_tmpCardCount,_tmpCreatedAt);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}

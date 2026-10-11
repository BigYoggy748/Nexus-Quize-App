package com.nexus.quiz.data.local.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.nexus.quiz.data.local.Converters;
import com.nexus.quiz.data.local.entity.ReviewLogEntity;
import com.nexus.quiz.data.model.SrsRating;
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
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class ReviewLogDao_Impl implements ReviewLogDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ReviewLogEntity> __insertionAdapterOfReviewLogEntity;

  private final Converters __converters = new Converters();

  public ReviewLogDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfReviewLogEntity = new EntityInsertionAdapter<ReviewLogEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `review_logs` (`id`,`cardId`,`rating`,`reviewTimestamp`,`intervalBefore`,`intervalAfter`,`easeFactorBefore`,`easeFactorAfter`,`timeSpentMs`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ReviewLogEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getCardId());
        final String _tmp = __converters.fromSrsRating(entity.getRating());
        statement.bindString(3, _tmp);
        statement.bindLong(4, entity.getReviewTimestamp());
        statement.bindLong(5, entity.getIntervalBefore());
        statement.bindLong(6, entity.getIntervalAfter());
        statement.bindDouble(7, entity.getEaseFactorBefore());
        statement.bindDouble(8, entity.getEaseFactorAfter());
        statement.bindLong(9, entity.getTimeSpentMs());
      }
    };
  }

  @Override
  public Object insertLog(final ReviewLogEntity log, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfReviewLogEntity.insertAndReturnId(log);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<ReviewLogEntity>> getLogsForCard(final long cardId) {
    final String _sql = "SELECT * FROM review_logs WHERE cardId = ? ORDER BY reviewTimestamp DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, cardId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"review_logs"}, new Callable<List<ReviewLogEntity>>() {
      @Override
      @NonNull
      public List<ReviewLogEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfCardId = CursorUtil.getColumnIndexOrThrow(_cursor, "cardId");
          final int _cursorIndexOfRating = CursorUtil.getColumnIndexOrThrow(_cursor, "rating");
          final int _cursorIndexOfReviewTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "reviewTimestamp");
          final int _cursorIndexOfIntervalBefore = CursorUtil.getColumnIndexOrThrow(_cursor, "intervalBefore");
          final int _cursorIndexOfIntervalAfter = CursorUtil.getColumnIndexOrThrow(_cursor, "intervalAfter");
          final int _cursorIndexOfEaseFactorBefore = CursorUtil.getColumnIndexOrThrow(_cursor, "easeFactorBefore");
          final int _cursorIndexOfEaseFactorAfter = CursorUtil.getColumnIndexOrThrow(_cursor, "easeFactorAfter");
          final int _cursorIndexOfTimeSpentMs = CursorUtil.getColumnIndexOrThrow(_cursor, "timeSpentMs");
          final List<ReviewLogEntity> _result = new ArrayList<ReviewLogEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ReviewLogEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpCardId;
            _tmpCardId = _cursor.getLong(_cursorIndexOfCardId);
            final SrsRating _tmpRating;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfRating);
            _tmpRating = __converters.toSrsRating(_tmp);
            final long _tmpReviewTimestamp;
            _tmpReviewTimestamp = _cursor.getLong(_cursorIndexOfReviewTimestamp);
            final int _tmpIntervalBefore;
            _tmpIntervalBefore = _cursor.getInt(_cursorIndexOfIntervalBefore);
            final int _tmpIntervalAfter;
            _tmpIntervalAfter = _cursor.getInt(_cursorIndexOfIntervalAfter);
            final float _tmpEaseFactorBefore;
            _tmpEaseFactorBefore = _cursor.getFloat(_cursorIndexOfEaseFactorBefore);
            final float _tmpEaseFactorAfter;
            _tmpEaseFactorAfter = _cursor.getFloat(_cursorIndexOfEaseFactorAfter);
            final long _tmpTimeSpentMs;
            _tmpTimeSpentMs = _cursor.getLong(_cursorIndexOfTimeSpentMs);
            _item = new ReviewLogEntity(_tmpId,_tmpCardId,_tmpRating,_tmpReviewTimestamp,_tmpIntervalBefore,_tmpIntervalAfter,_tmpEaseFactorBefore,_tmpEaseFactorAfter,_tmpTimeSpentMs);
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
  public Flow<List<ReviewLogEntity>> getRecentLogs(final int limit) {
    final String _sql = "SELECT * FROM review_logs ORDER BY reviewTimestamp DESC LIMIT ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, limit);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"review_logs"}, new Callable<List<ReviewLogEntity>>() {
      @Override
      @NonNull
      public List<ReviewLogEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfCardId = CursorUtil.getColumnIndexOrThrow(_cursor, "cardId");
          final int _cursorIndexOfRating = CursorUtil.getColumnIndexOrThrow(_cursor, "rating");
          final int _cursorIndexOfReviewTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "reviewTimestamp");
          final int _cursorIndexOfIntervalBefore = CursorUtil.getColumnIndexOrThrow(_cursor, "intervalBefore");
          final int _cursorIndexOfIntervalAfter = CursorUtil.getColumnIndexOrThrow(_cursor, "intervalAfter");
          final int _cursorIndexOfEaseFactorBefore = CursorUtil.getColumnIndexOrThrow(_cursor, "easeFactorBefore");
          final int _cursorIndexOfEaseFactorAfter = CursorUtil.getColumnIndexOrThrow(_cursor, "easeFactorAfter");
          final int _cursorIndexOfTimeSpentMs = CursorUtil.getColumnIndexOrThrow(_cursor, "timeSpentMs");
          final List<ReviewLogEntity> _result = new ArrayList<ReviewLogEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ReviewLogEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpCardId;
            _tmpCardId = _cursor.getLong(_cursorIndexOfCardId);
            final SrsRating _tmpRating;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfRating);
            _tmpRating = __converters.toSrsRating(_tmp);
            final long _tmpReviewTimestamp;
            _tmpReviewTimestamp = _cursor.getLong(_cursorIndexOfReviewTimestamp);
            final int _tmpIntervalBefore;
            _tmpIntervalBefore = _cursor.getInt(_cursorIndexOfIntervalBefore);
            final int _tmpIntervalAfter;
            _tmpIntervalAfter = _cursor.getInt(_cursorIndexOfIntervalAfter);
            final float _tmpEaseFactorBefore;
            _tmpEaseFactorBefore = _cursor.getFloat(_cursorIndexOfEaseFactorBefore);
            final float _tmpEaseFactorAfter;
            _tmpEaseFactorAfter = _cursor.getFloat(_cursorIndexOfEaseFactorAfter);
            final long _tmpTimeSpentMs;
            _tmpTimeSpentMs = _cursor.getLong(_cursorIndexOfTimeSpentMs);
            _item = new ReviewLogEntity(_tmpId,_tmpCardId,_tmpRating,_tmpReviewTimestamp,_tmpIntervalBefore,_tmpIntervalAfter,_tmpEaseFactorBefore,_tmpEaseFactorAfter,_tmpTimeSpentMs);
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
  public Flow<ReviewAccuracyStats> getAccuracyStats() {
    final String _sql = "\n"
            + "        SELECT \n"
            + "            COUNT(*) as totalReviews,\n"
            + "            SUM(CASE WHEN rating = 'AGAIN' THEN 1 ELSE 0 END) as againCount,\n"
            + "            SUM(CASE WHEN rating = 'HARD' THEN 1 ELSE 0 END) as hardCount,\n"
            + "            SUM(CASE WHEN rating = 'GOOD' THEN 1 ELSE 0 END) as goodCount,\n"
            + "            SUM(CASE WHEN rating = 'EASY' THEN 1 ELSE 0 END) as easyCount\n"
            + "        FROM review_logs\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"review_logs"}, new Callable<ReviewAccuracyStats>() {
      @Override
      @NonNull
      public ReviewAccuracyStats call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfTotalReviews = 0;
          final int _cursorIndexOfAgainCount = 1;
          final int _cursorIndexOfHardCount = 2;
          final int _cursorIndexOfGoodCount = 3;
          final int _cursorIndexOfEasyCount = 4;
          final ReviewAccuracyStats _result;
          if (_cursor.moveToFirst()) {
            final int _tmpTotalReviews;
            _tmpTotalReviews = _cursor.getInt(_cursorIndexOfTotalReviews);
            final int _tmpAgainCount;
            _tmpAgainCount = _cursor.getInt(_cursorIndexOfAgainCount);
            final int _tmpHardCount;
            _tmpHardCount = _cursor.getInt(_cursorIndexOfHardCount);
            final int _tmpGoodCount;
            _tmpGoodCount = _cursor.getInt(_cursorIndexOfGoodCount);
            final int _tmpEasyCount;
            _tmpEasyCount = _cursor.getInt(_cursorIndexOfEasyCount);
            _result = new ReviewAccuracyStats(_tmpTotalReviews,_tmpAgainCount,_tmpHardCount,_tmpGoodCount,_tmpEasyCount);
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
  public Flow<Integer> countReviewsSince(final long sinceTimestamp) {
    final String _sql = "\n"
            + "        SELECT COUNT(*) FROM review_logs \n"
            + "        WHERE reviewTimestamp >= ?\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, sinceTimestamp);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"review_logs"}, new Callable<Integer>() {
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}

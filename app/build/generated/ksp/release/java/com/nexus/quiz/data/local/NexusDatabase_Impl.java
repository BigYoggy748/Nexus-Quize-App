package com.nexus.quiz.data.local;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.nexus.quiz.data.local.dao.CardDao;
import com.nexus.quiz.data.local.dao.CardDao_Impl;
import com.nexus.quiz.data.local.dao.DeckDao;
import com.nexus.quiz.data.local.dao.DeckDao_Impl;
import com.nexus.quiz.data.local.dao.ReviewLogDao;
import com.nexus.quiz.data.local.dao.ReviewLogDao_Impl;
import com.nexus.quiz.data.local.dao.TopicMasteryDao;
import com.nexus.quiz.data.local.dao.TopicMasteryDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class NexusDatabase_Impl extends NexusDatabase {
  private volatile DeckDao _deckDao;

  private volatile CardDao _cardDao;

  private volatile ReviewLogDao _reviewLogDao;

  private volatile TopicMasteryDao _topicMasteryDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(2) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `decks` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `description` TEXT NOT NULL, `category` TEXT NOT NULL, `hexColor` TEXT NOT NULL, `createdAt` INTEGER NOT NULL, `updatedAt` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `cards` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `deckId` INTEGER NOT NULL, `type` TEXT NOT NULL, `prompt` TEXT NOT NULL, `answer` TEXT NOT NULL, `explanation` TEXT NOT NULL, `latexFormula` TEXT, `clozeOcclusion` TEXT, `derivationStepsJson` TEXT, `keyTermsJson` TEXT, `misconceptionsJson` TEXT, `difficulty` TEXT NOT NULL, `interval` INTEGER NOT NULL, `repetitions` INTEGER NOT NULL, `easeFactor` REAL NOT NULL, `lapses` INTEGER NOT NULL, `dueDate` INTEGER NOT NULL, `lastReviewedDate` INTEGER, `createdAt` INTEGER NOT NULL, FOREIGN KEY(`deckId`) REFERENCES `decks`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_cards_deckId` ON `cards` (`deckId`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_cards_dueDate` ON `cards` (`dueDate`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_cards_type` ON `cards` (`type`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `review_logs` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `cardId` INTEGER NOT NULL, `rating` TEXT NOT NULL, `reviewTimestamp` INTEGER NOT NULL, `intervalBefore` INTEGER NOT NULL, `intervalAfter` INTEGER NOT NULL, `easeFactorBefore` REAL NOT NULL, `easeFactorAfter` REAL NOT NULL, `timeSpentMs` INTEGER NOT NULL, FOREIGN KEY(`cardId`) REFERENCES `cards`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_review_logs_cardId` ON `review_logs` (`cardId`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_review_logs_reviewTimestamp` ON `review_logs` (`reviewTimestamp`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `saved_topics` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `topicTitle` TEXT NOT NULL, `topicHint` TEXT NOT NULL, `rawNotes` TEXT NOT NULL, `jsonContent` TEXT NOT NULL, `cardCount` INTEGER NOT NULL, `createdAt` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'be7652775afe7927846c5e2560f11050')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `decks`");
        db.execSQL("DROP TABLE IF EXISTS `cards`");
        db.execSQL("DROP TABLE IF EXISTS `review_logs`");
        db.execSQL("DROP TABLE IF EXISTS `saved_topics`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        db.execSQL("PRAGMA foreign_keys = ON");
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsDecks = new HashMap<String, TableInfo.Column>(7);
        _columnsDecks.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDecks.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDecks.put("description", new TableInfo.Column("description", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDecks.put("category", new TableInfo.Column("category", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDecks.put("hexColor", new TableInfo.Column("hexColor", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDecks.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDecks.put("updatedAt", new TableInfo.Column("updatedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysDecks = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesDecks = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoDecks = new TableInfo("decks", _columnsDecks, _foreignKeysDecks, _indicesDecks);
        final TableInfo _existingDecks = TableInfo.read(db, "decks");
        if (!_infoDecks.equals(_existingDecks)) {
          return new RoomOpenHelper.ValidationResult(false, "decks(com.nexus.quiz.data.local.entity.DeckEntity).\n"
                  + " Expected:\n" + _infoDecks + "\n"
                  + " Found:\n" + _existingDecks);
        }
        final HashMap<String, TableInfo.Column> _columnsCards = new HashMap<String, TableInfo.Column>(19);
        _columnsCards.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCards.put("deckId", new TableInfo.Column("deckId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCards.put("type", new TableInfo.Column("type", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCards.put("prompt", new TableInfo.Column("prompt", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCards.put("answer", new TableInfo.Column("answer", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCards.put("explanation", new TableInfo.Column("explanation", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCards.put("latexFormula", new TableInfo.Column("latexFormula", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCards.put("clozeOcclusion", new TableInfo.Column("clozeOcclusion", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCards.put("derivationStepsJson", new TableInfo.Column("derivationStepsJson", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCards.put("keyTermsJson", new TableInfo.Column("keyTermsJson", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCards.put("misconceptionsJson", new TableInfo.Column("misconceptionsJson", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCards.put("difficulty", new TableInfo.Column("difficulty", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCards.put("interval", new TableInfo.Column("interval", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCards.put("repetitions", new TableInfo.Column("repetitions", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCards.put("easeFactor", new TableInfo.Column("easeFactor", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCards.put("lapses", new TableInfo.Column("lapses", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCards.put("dueDate", new TableInfo.Column("dueDate", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCards.put("lastReviewedDate", new TableInfo.Column("lastReviewedDate", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCards.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysCards = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysCards.add(new TableInfo.ForeignKey("decks", "CASCADE", "NO ACTION", Arrays.asList("deckId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesCards = new HashSet<TableInfo.Index>(3);
        _indicesCards.add(new TableInfo.Index("index_cards_deckId", false, Arrays.asList("deckId"), Arrays.asList("ASC")));
        _indicesCards.add(new TableInfo.Index("index_cards_dueDate", false, Arrays.asList("dueDate"), Arrays.asList("ASC")));
        _indicesCards.add(new TableInfo.Index("index_cards_type", false, Arrays.asList("type"), Arrays.asList("ASC")));
        final TableInfo _infoCards = new TableInfo("cards", _columnsCards, _foreignKeysCards, _indicesCards);
        final TableInfo _existingCards = TableInfo.read(db, "cards");
        if (!_infoCards.equals(_existingCards)) {
          return new RoomOpenHelper.ValidationResult(false, "cards(com.nexus.quiz.data.local.entity.CardEntity).\n"
                  + " Expected:\n" + _infoCards + "\n"
                  + " Found:\n" + _existingCards);
        }
        final HashMap<String, TableInfo.Column> _columnsReviewLogs = new HashMap<String, TableInfo.Column>(9);
        _columnsReviewLogs.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReviewLogs.put("cardId", new TableInfo.Column("cardId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReviewLogs.put("rating", new TableInfo.Column("rating", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReviewLogs.put("reviewTimestamp", new TableInfo.Column("reviewTimestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReviewLogs.put("intervalBefore", new TableInfo.Column("intervalBefore", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReviewLogs.put("intervalAfter", new TableInfo.Column("intervalAfter", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReviewLogs.put("easeFactorBefore", new TableInfo.Column("easeFactorBefore", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReviewLogs.put("easeFactorAfter", new TableInfo.Column("easeFactorAfter", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReviewLogs.put("timeSpentMs", new TableInfo.Column("timeSpentMs", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysReviewLogs = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysReviewLogs.add(new TableInfo.ForeignKey("cards", "CASCADE", "NO ACTION", Arrays.asList("cardId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesReviewLogs = new HashSet<TableInfo.Index>(2);
        _indicesReviewLogs.add(new TableInfo.Index("index_review_logs_cardId", false, Arrays.asList("cardId"), Arrays.asList("ASC")));
        _indicesReviewLogs.add(new TableInfo.Index("index_review_logs_reviewTimestamp", false, Arrays.asList("reviewTimestamp"), Arrays.asList("ASC")));
        final TableInfo _infoReviewLogs = new TableInfo("review_logs", _columnsReviewLogs, _foreignKeysReviewLogs, _indicesReviewLogs);
        final TableInfo _existingReviewLogs = TableInfo.read(db, "review_logs");
        if (!_infoReviewLogs.equals(_existingReviewLogs)) {
          return new RoomOpenHelper.ValidationResult(false, "review_logs(com.nexus.quiz.data.local.entity.ReviewLogEntity).\n"
                  + " Expected:\n" + _infoReviewLogs + "\n"
                  + " Found:\n" + _existingReviewLogs);
        }
        final HashMap<String, TableInfo.Column> _columnsSavedTopics = new HashMap<String, TableInfo.Column>(7);
        _columnsSavedTopics.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSavedTopics.put("topicTitle", new TableInfo.Column("topicTitle", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSavedTopics.put("topicHint", new TableInfo.Column("topicHint", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSavedTopics.put("rawNotes", new TableInfo.Column("rawNotes", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSavedTopics.put("jsonContent", new TableInfo.Column("jsonContent", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSavedTopics.put("cardCount", new TableInfo.Column("cardCount", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSavedTopics.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysSavedTopics = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesSavedTopics = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoSavedTopics = new TableInfo("saved_topics", _columnsSavedTopics, _foreignKeysSavedTopics, _indicesSavedTopics);
        final TableInfo _existingSavedTopics = TableInfo.read(db, "saved_topics");
        if (!_infoSavedTopics.equals(_existingSavedTopics)) {
          return new RoomOpenHelper.ValidationResult(false, "saved_topics(com.nexus.quiz.data.local.entity.TopicMasteryEntity).\n"
                  + " Expected:\n" + _infoSavedTopics + "\n"
                  + " Found:\n" + _existingSavedTopics);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "be7652775afe7927846c5e2560f11050", "cd29b5b235f9b86a99d73c3f26b946e5");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "decks","cards","review_logs","saved_topics");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    final boolean _supportsDeferForeignKeys = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP;
    try {
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = FALSE");
      }
      super.beginTransaction();
      if (_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA defer_foreign_keys = TRUE");
      }
      _db.execSQL("DELETE FROM `decks`");
      _db.execSQL("DELETE FROM `cards`");
      _db.execSQL("DELETE FROM `review_logs`");
      _db.execSQL("DELETE FROM `saved_topics`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = TRUE");
      }
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(DeckDao.class, DeckDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(CardDao.class, CardDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ReviewLogDao.class, ReviewLogDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(TopicMasteryDao.class, TopicMasteryDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public DeckDao deckDao() {
    if (_deckDao != null) {
      return _deckDao;
    } else {
      synchronized(this) {
        if(_deckDao == null) {
          _deckDao = new DeckDao_Impl(this);
        }
        return _deckDao;
      }
    }
  }

  @Override
  public CardDao cardDao() {
    if (_cardDao != null) {
      return _cardDao;
    } else {
      synchronized(this) {
        if(_cardDao == null) {
          _cardDao = new CardDao_Impl(this);
        }
        return _cardDao;
      }
    }
  }

  @Override
  public ReviewLogDao reviewLogDao() {
    if (_reviewLogDao != null) {
      return _reviewLogDao;
    } else {
      synchronized(this) {
        if(_reviewLogDao == null) {
          _reviewLogDao = new ReviewLogDao_Impl(this);
        }
        return _reviewLogDao;
      }
    }
  }

  @Override
  public TopicMasteryDao topicMasteryDao() {
    if (_topicMasteryDao != null) {
      return _topicMasteryDao;
    } else {
      synchronized(this) {
        if(_topicMasteryDao == null) {
          _topicMasteryDao = new TopicMasteryDao_Impl(this);
        }
        return _topicMasteryDao;
      }
    }
  }
}

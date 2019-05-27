package com.example.ahmeda47.gastracker;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;

@Database(entities = {Transaction.class}, version = 1)
public abstract class MyAppDatabase extends RoomDatabase
{

    public abstract MyDao myDao();

   // static final Migration MIGRATION_1_2 = new Migration(1, 2) {
      //  @Override
      //  public void migrate(SupportSQLiteDatabase database) {
            // Since we didn't alter the table, there's nothing else to do here.
       // }
    };
//}

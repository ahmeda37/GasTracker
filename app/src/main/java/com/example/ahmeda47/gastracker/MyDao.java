package com.example.ahmeda47.gastracker;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface MyDao
{

    @Insert
    public void addTransaction(Transaction transaction);

    @Query("delete from users where id like :id")
    public int deleteTransaction(int id);

    @Query("select * from users")
    public List<Transaction> getTransactions();

    @Query("select * from users where month like :month and year like :year")
    public List<Transaction> getTransactionForMonth(String month, String year);

    @Query("select * from users where year like :year")
    public List<Transaction> getTransactionForYear(String year);

    @Query("select * from users where month like :month and year like :year and costType like :costType")
    public List<Transaction> getTransactionForMonthType(String month, String year, String costType);
}

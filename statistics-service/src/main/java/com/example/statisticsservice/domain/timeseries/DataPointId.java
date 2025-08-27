package com.example.statisticsservice.domain.timeseries;

import com.example.statisticsservice.domain.Account;
import jakarta.validation.Valid;

import java.util.Date;

public class DataPointId {

    private static final long serialVersionUID = 1L;

    private final String account;
    private final Date date;

    public DataPointId(String account, Date date) {
        this.account = account;
        this.date = date;
    }

    public String getAccount() {
        return account;
    }

    public Date getDate() {
        return date;
    }


    @Override
    public String toString() {
        return "DataPointId{" +
                "account='" + account + '\'' +
                ", date=" + date +
                '}';

    }


}

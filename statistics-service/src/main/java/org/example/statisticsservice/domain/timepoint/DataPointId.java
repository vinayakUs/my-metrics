package org.example.statisticsservice.domain.timepoint;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.example.statisticsservice.domain.Account;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

@Setter
@Getter
public class DataPointId implements Serializable {
    private static final long serialVersionUID = 1L;
    private String account;
    private Date date;
    public DataPointId(String account, Date date) {
        this.account = account;
        this.date = date;
    }



    @Override
    public String toString() {
        return "DataPointId{" +
                "account='" + account + '\'' +
                ", date=" + date +
                '}';
    }
}

package com.songzi.web.rest.vm;

import com.songzi.domain.Report;
import com.songzi.domain.ReportItems;

import java.util.List;

/**
 * Created by qingyuan on 19/3/31.
 */
public class ReportDetailVM {
    private Report report;

    private List<ReportItems> reportItemsList;

    public ReportDetailVM(Report report, List<ReportItems> reportItemsList) {
        this.report = report;
        this.reportItemsList = reportItemsList;
    }

    public Report getReport() {
        return report;
    }

    public void setReport(Report report) {
        this.report = report;
    }

    public List<ReportItems> getReportItemsList() {
        return reportItemsList;
    }

    public void setReportItemsList(List<ReportItems> reportItemsList) {
        this.reportItemsList = reportItemsList;
    }
}

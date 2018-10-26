package com.sun.domain;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QSDomain {
    
    private Project project;

    private Employee employee;
    
    private Date monthly;
    
    private QualitySafetyPlan plan;

    private QualitySafety check;

    private QualitySafetyCheckResult checkResult;

    private QualitySafetyChange change;
    
    private Date lastDate;
    
    private Date beginDate;
    
    private Date lastTime;
    
	private Date recordDate;


	@Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class QualitySafetyPlan {

        private Integer allQty;

    }
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class QualitySafety {

        private Integer totalQty;

        private Integer oncePassQty;

        private Integer changeQty;

        private Integer manyTimesChangeQty;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class QualitySafetyCheckResult {
        private Integer totalQty;

        private Integer passQty;

        private Integer warningQty;

        private Integer changeQty;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class QualitySafetyChange {
        private Integer manyTimesQty;

        private Integer oncePassQty;

        private Integer overdueQty;
    }


}

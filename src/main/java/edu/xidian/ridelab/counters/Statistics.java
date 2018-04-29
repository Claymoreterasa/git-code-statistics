package edu.xidian.ridelab.counters;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Author: cwz
 * Time: 2018/4/29
 * Description: 信息统计类，包含有效代码行，注释行，空行数量
 */

@AllArgsConstructor
@Data
public class Statistics {
    private long validLines;
    private long commentLines;
    private long emptyLines;

    public Statistics add(Statistics statistics){
        validLines += statistics.validLines;
        commentLines += statistics.commentLines;
        emptyLines += statistics.emptyLines;
        return this;
    }
}

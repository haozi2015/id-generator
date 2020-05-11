package com.haozi.id.generator.core.rule.repository.mysql;

import com.haozi.id.generator.core.rule.repository.SequenceRule;

import java.util.Map;

public class SequenceRuleSQL {

    public String insert(SequenceRule record) {
        StringBuffer filed = new StringBuffer();
        StringBuffer value = new StringBuffer();
        if (record.getKey() != null) {
            filed.append("`key`,");
            value.append("'").append(record.getKey()).append("'").append(",");
        }
        if (record.getIncrement() != null) {
            filed.append("increment,");
            value.append(record.getIncrement()).append(",");
        }
        if (record.getMemoryCapacity() != null) {
            filed.append("memory_capacity,");
            value.append(record.getMemoryCapacity()).append(",");
        }
        if (record.getReloadThreshold() != null) {
            filed.append("reload_threshold,");
            value.append(record.getReloadThreshold()).append(",");
        }
        if (record.getPrefix() != null) {
            filed.append("prefix,");
            value.append("'").append(record.getPrefix()).append("'").append(",");
        }
        if (record.getDigits() != null) {
            filed.append("digits,");
            value.append(record.getDigits()).append(",");
        }
        if (record.getResetRule() != null) {
            filed.append("reset_rule,");
            value.append("'").append(record.getResetRule()).append("'").append(",");
        }
        filed.append("status");
        value.append("0");
        return "insert into t_sequence_rule(" + filed.toString() + ") values(" + value.toString() + ")";
    }

    public String updateByKey(SequenceRule record) {
        StringBuffer update = new StringBuffer();

        if (record.getIncrement() != null) {
            update.append("increment=").append(record.getIncrement()).append(",");
        }
        if (record.getMemoryCapacity() != null) {
            update.append("memory_capacity=").append(record.getMemoryCapacity()).append(",");
        }
        if (record.getReloadThreshold() != null) {
            update.append("reload_threshold=").append(record.getReloadThreshold()).append(",");
        }
        if (record.getPrefix() != null) {
            update.append("prefix=").append("'").append(record.getPrefix()).append("'").append(",");
        }
        if (record.getDigits() != null) {
            update.append("digits=").append(record.getDigits()).append(",");
        }
        if (record.getResetRule() != null) {
            update.append("reset_rule=").append("'").append(record.getResetRule()).append("'").append(",");
        }
        if (record.getStatus() != null) {
            update.append("status=").append(record.getStatus()).append(",");
        }
        return "update t_sequence_rule set " + update.substring(0, update.length() - 1) + " where `key`='" + record.getKey() + "'";
    }

    public String selectByPage(Map param) {
        StringBuffer sql = new StringBuffer("select * from t_sequence_rule ");
        if (param.get("key") != null || param.get("status") != null) {
            sql.append("where 1=1");
        }

        if (param.get("key") != null) {
            sql.append(" and `key`='").append(param.get("key")).append("'");
        }
        if (param.get("status") != null) {
            sql.append(" and status=").append(param.get("status"));
        }
        sql.append(" order by id desc ");
        sql.append("limit ");
        sql.append(param.get("row"));
        sql.append(",");
        sql.append(param.get("pageSize"));
        return sql.toString();
    }

    public String selectByCount(Map param) {
        StringBuffer sql = new StringBuffer("select count(*) from t_sequence_rule ");
        if (param.get("key") != null || param.get("status") != null) {
            sql.append("where 1=1");
        }

        if (param.get("key") != null) {
            sql.append(" and `key`='").append(param.get("key")).append("'");
        }
        if (param.get("status") != null) {
            sql.append(" and status=").append(param.get("status"));
        }
        return sql.toString();
    }
}
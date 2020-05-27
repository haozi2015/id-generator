package com.haozi.id.generator.core.rule.repository.mysql;

import com.haozi.id.generator.core.rule.repository.SequenceRule;

import java.util.Map;

public class SequenceRuleSQL {

    public String insert(SequenceRule record) {
        StringBuffer filed = new StringBuffer();
        StringBuffer value = new StringBuffer();
        if (record.getKey() != null) {
            filed.append("`key`,");
            value.append("#{key},");
        }
        if (record.getIncrement() != null) {
            filed.append("increment,");
            value.append("#{increment},");
        }
        if (record.getMemoryCapacity() != null) {
            filed.append("memory_capacity,");
            value.append("#{memoryCapacity},");
        }
        if (record.getReloadThreshold() != null) {
            filed.append("reload_threshold,");
            value.append("#{reloadThreshold},");
        }
        if (record.getPrefix() != null) {
            filed.append("prefix,");
            value.append("#{prefix},");
        }
        if (record.getDigits() != null) {
            filed.append("digits,");
            value.append("#{digits},");
        }
        if (record.getResetRule() != null) {
            filed.append("reset_rule,");
            value.append("#{resetRule},");
        }
        if (record.getStatus() != null) {
            filed.append("status,");
            value.append("#{status},");
        }
        if (record.getLastUpdateTime() != null) {
            filed.append("last_update_time,");
            value.append("#{lastUpdateTime},");
        }
        if (record.getInitialValue() != null) {
            filed.append("initial_value,");
            value.append("#{initialValue},");
        }
        return "insert into t_sequence_rule(" + filed.toString() + ") values(" + value.toString() + ")";
    }

    public String updateByKey(SequenceRule record) {
        StringBuffer update = new StringBuffer();

        if (record.getIncrement() != null) {
            update.append("increment=#{increment},");
        }
        if (record.getMemoryCapacity() != null) {
            update.append("memory_capacity=#{memoryCapacity},");
        }
        if (record.getReloadThreshold() != null) {
            update.append("reload_threshold=#{reloadThreshold},");
        }
        if (record.getPrefix() != null) {
            update.append("prefix=#{prefix},");
        }
        if (record.getDigits() != null) {
            update.append("digits=#{digits},");
        }
        if (record.getResetRule() != null) {
            update.append("reset_rule=#{resetRule},");
        }
        if (record.getStatus() != null) {
            update.append("status=#{status},");
        }
        if (record.getLastUpdateTime() != null) {
            update.append("last_update_time=#{lastUpdateTime},");
        }
        if (record.getInitialValue() != null) {
            update.append("initial_value=#{initialValue},");
        }
        return "update t_sequence_rule set " + update.substring(0, update.length() - 1) + " where `key`=#{key}";
    }

    public String selectByPage(Map param) {
        StringBuffer sql = new StringBuffer("select * from t_sequence_rule ");
        if (param.get("key") != null || param.get("status") != null) {
            sql.append("where 1=1");
        }

        if (param.get("key") != null) {
            sql.append(" and `key`=#{key}");
        }
        if (param.get("status") != null) {
            sql.append(" and status=#{status}");
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
            sql.append(" and `key`=#{key}");
        }
        if (param.get("status") != null) {
            sql.append(" and status=#{status}");
        }
        return sql.toString();
    }
}
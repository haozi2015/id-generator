package com.haozi.id.generator.core.sequence.dao;

public class SequenceRuleDefinitionSQL {

    public String insert(SequenceRuleDefinition record) {
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
        if (record.getReloadThresholdRate() != null) {
            filed.append("reload_threshold_rate,");
            value.append(record.getReloadThresholdRate()).append(",");
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

    public String updateByKey(SequenceRuleDefinition record) {
        StringBuffer update = new StringBuffer();

        if (record.getIncrement() != null) {
            update.append("increment=").append(record.getIncrement()).append(",");
        }
        if (record.getMemoryCapacity() != null) {
            update.append("memory_capacity=").append(record.getMemoryCapacity()).append(",");
        }
        if (record.getReloadThresholdRate() != null) {
            update.append("reload_threshold_rate=").append(record.getReloadThresholdRate()).append(",");
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
        System.out.println("update t_sequence_rule set " + update.substring(0, update.length() - 1) + " where `key`='" + record.getKey() + "'");
        return "update t_sequence_rule set " + update.substring(0, update.length() - 1) + " where `key`='" + record.getKey() + "'";
    }
}
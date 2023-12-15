package com.santint.colorformula.enums;

/**
 * author： cyw
 */
public enum BreakCodeEnum implements BaseEnum {
    Emegency(1, "Emegency", "E01:急停开关并未打开"), StopCannedFail(2, "StopCannedFail", "E02:终止罐装失败"),
    Flood(3, "Flood", "涨发"), DoorOpen(4, "DoorOpen", "E04:门未关"),
    SpliceBoxBreak(5, "SpliceBoxBreak", "E05:接料盒故障"), Other(6, "Other", "其他动作"),
    DipenserBushy(7, "DipenserBushy", "E07:调色机忙"), CheckSum(8, "CheckSum", "E08:校验和错误"),
    NetAmountWeight(9, "NetAmountWeight", "净量称重"), SlicedWithName(10, "SlicedWithName", "切制（带名称）"),
    BarrelNot(11, "BarrelNot", "请放桶"), MotorBreak(12, "MotorBreak", "E80:注出电机故障"),
    TurnMotorBreak(13, "TurnMotorBreak", "E81:转盘电机故障或手柄未到位"), EncodeBreak(14, "EncodeBreak", "E82:桶位编码器故障"),
    TelescopicBreak(15, "TelescopicBreak", "E84:伸缩机构故障"), ElectronicBreak(16, "ElectronicBreak", "E86:电子秤通讯异常"),
    ElectronicOver(17, "ElectronicOver", "E87:电子秤超载"), ElectronicUnder(18, "ElectronicUnder", "E88:电子秤欠载"),
    EmptyOver(19, "EmptyOver", "E85:放置空桶超重"), BarcodeScanner(20, "BarcodeScanner", "E90:扫码器故障"), OutletSealing(21, "OutletSealing", "E91:出料口封板故障"),
    BarrelRemoved(22, "BarrelRemoved", "E92:配料中，桶被移走"),ValveBreak(23,"ValveBreak","E94:阀门故障");
    private final Integer value;
    private final String text;
    private final String description;

    BreakCodeEnum(Integer value, String text, String description) {
        this.value = value;
        this.text = text;
        this.description = description;
    }


    @Override
    public Integer getValue() {
        return this.value;
    }

    @Override
    public String getText() {
        return this.text;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    public static BreakCodeEnum valueOf(Integer value) {
        BreakCodeEnum[] buzhouEnums = BreakCodeEnum.values();
        for (BreakCodeEnum e : buzhouEnums) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }
        return null;
    }
}

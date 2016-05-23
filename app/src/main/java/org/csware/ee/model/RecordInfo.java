package org.csware.ee.model;

import java.util.List;

/**
 * Created by zhojiulong on 2016/4/7.
 */
public class RecordInfo extends Return {


    /**
     *  "AccountLog": [
     {
     "Id": 58,
     "UserId": 10006,
     "Type": 0,
     "Change": -0.0100,
     "Original": 0.0000,
     "Addon": "订单:23868:212",
     "Time": 1459926222
     },
     {
     "Id": 57,
     "UserId": 10006,
     "Type": 0,
     "Change": 0.0100,
     "Original": 0.0000,
     "Addon": "订单:23868:212",
     "Time": 1459926222
     },
     {
     "Id": 56,
     "UserId": 10006,
     "Type": 0,
     "Change": -120.0000,
     "Original": 2900.0000,
     "Addon": "提现",
     "Time": 1459913740
     },
     {
     "Id": 55,
     "UserId": 10006,
     "Type": 0,
     "Change": -122.0000,
     "Original": 3022.0000,
     "Addon": "提现",
     "Time": 1459913716
     },
     {
     "Id": 54,
     "UserId": 10006,
     "Type": 0,
     "Change": -100.0000,
     "Original": 3122.0000,
     "Addon": "提现",
     "Time": 1459913170
     },
     {
     "Id": 53,
     "UserId": 10006,
     "Type": 0,
     "Change": -100.0000,
     "Original": 3222.0000,
     "Addon": "提现",
     "Time": 1459913153
     },
     {
     "Id": 52,
     "UserId": 10006,
     "Type": 0,
     "Change": -100.0000,
     "Original": 3322.0000,
     "Addon": "提现",
     "Time": 1459850663
     },
     {
     "Id": 51,
     "UserId": 10006,
     "Type": 0,
     "Change": -100.0000,
     "Original": 3422.0000,
     "Addon": "提现",
     "Time": 1459850600
     },
     {
     "Id": 50,
     "UserId": 10006,
     "Type": 0,
     "Change": -100.0000,
     "Original": 3522.0000,
     "Addon": "提现",
     "Time": 1459849869
     },
     {
     "Id": 49,
     "UserId": 10006,
     "Type": 0,
     "Change": -100.0000,
     "Original": 3622.0000,
     "Addon": "提现",
     "Time": 1459848476
     },
     {
     "Id": 48,
     "UserId": 10006,
     "Type": 0,
     "Change": -1.0000,
     "Original": 3623.0000,
     "Addon": "提现",
     "Time": 1459848385
     },
     {
     "Id": 47,
     "UserId": 10006,
     "Type": 0,
     "Change": -1.0000,
     "Original": 3624.0000,
     "Addon": "提现",
     "Time": 1459847083
     },
     {
     "Id": 46,
     "UserId": 10006,
     "Type": 0,
     "Change": -1.0000,
     "Original": 3625.0000,
     "Addon": "提现",
     "Time": 1459847074
     },
     {
     "Id": 45,
     "UserId": 10006,
     "Type": 0,
     "Change": -1.0000,
     "Original": 3626.0000,
     "Addon": "提现",
     "Time": 1459847031
     },
     {
     "Id": 44,
     "UserId": 10006,
     "Type": 0,
     "Change": -1.0000,
     "Original": 3627.0000,
     "Addon": "提现",
     "Time": 1459846955
     },
     {
     "Id": 43,
     "UserId": 10006,
     "Type": 0,
     "Change": -1.0000,
     "Original": 3628.0000,
     "Addon": "提现",
     "Time": 1459846935
     },
     {
     "Id": 42,
     "UserId": 10006,
     "Type": 0,
     "Change": -1.0000,
     "Original": 3629.0000,
     "Addon": "提现",
     "Time": 1459846393
     },
     {
     "Id": 41,
     "UserId": 10006,
     "Type": 0,
     "Change": -1.0000,
     "Original": 3630.0000,
     "Addon": "提现",
     "Time": 1459846298
     },
     {
     "Id": 40,
     "UserId": 10006,
     "Type": 0,
     "Change": -1.0000,
     "Original": 3631.0000,
     "Addon": "提现",
     "Time": 1459846149
     },
     {
     "Id": 39,
     "UserId": 10006,
     "Type": 0,
     "Change": -1.0000,
     "Original": 3632.0000,
     "Addon": "提现",
     "Time": 1459846135
     }
     ],
     */
    public List<AccountLogEntity> AccountLog;

    public static class AccountLogEntity {
 /*       "Id": 58,
                "UserId": 10006,
                "Type": 0,
                "Change": -0.0100,
                "Original": 0.0000,
                "Addon": "订单:23868:212",
                "Time": 1459926222*/
        public String Addon;
        public int UserId;
        public String Time;
        public int Id;
        public double Change;
        public double Original;
        public int Type;
    }
}

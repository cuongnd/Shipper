package org.csware.ee.consts;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yu on 2015/5/26.
 */
public class ChinaArea {

    public ChinaArea() {

    }

    void initProvinces() {
        //增加省
        PROVINCES.add(new AreaItem(0, 110000, "北京市"));
        PROVINCES.add(new AreaItem(0, 120000, "天津市"));
        PROVINCES.add(new AreaItem(0, 130000, "河北省"));
        PROVINCES.add(new AreaItem(0, 140000, "山西省"));
        PROVINCES.add(new AreaItem(0, 150000, "内蒙古自治区"));
        PROVINCES.add(new AreaItem(0, 210000, "辽宁省"));
        PROVINCES.add(new AreaItem(0, 220000, "吉林省"));
        PROVINCES.add(new AreaItem(0, 230000, "黑龙江省"));
        PROVINCES.add(new AreaItem(0, 310000, "上海市"));
        PROVINCES.add(new AreaItem(0, 320000, "江苏省"));
        PROVINCES.add(new AreaItem(0, 330000, "浙江省"));
        PROVINCES.add(new AreaItem(0, 340000, "安徽省"));
        PROVINCES.add(new AreaItem(0, 350000, "福建省"));
        PROVINCES.add(new AreaItem(0, 360000, "江西省"));
        PROVINCES.add(new AreaItem(0, 370000, "山东省"));
        PROVINCES.add(new AreaItem(0, 410000, "河南省"));
        PROVINCES.add(new AreaItem(0, 420000, "湖北省"));
        PROVINCES.add(new AreaItem(0, 430000, "湖南省"));
        PROVINCES.add(new AreaItem(0, 440000, "广东省"));
        PROVINCES.add(new AreaItem(0, 450000, "广西壮族自治区"));
        PROVINCES.add(new AreaItem(0, 460000, "海南省"));
        PROVINCES.add(new AreaItem(0, 500000, "重庆市"));
        PROVINCES.add(new AreaItem(0, 510000, "四川省"));
        PROVINCES.add(new AreaItem(0, 520000, "贵州省"));
        PROVINCES.add(new AreaItem(0, 530000, "云南省"));
        PROVINCES.add(new AreaItem(0, 540000, "西藏自治区"));
        PROVINCES.add(new AreaItem(0, 610000, "陕西省"));
        PROVINCES.add(new AreaItem(0, 620000, "甘肃省"));
        PROVINCES.add(new AreaItem(0, 630000, "青海省"));
        PROVINCES.add(new AreaItem(0, 640000, "宁夏回族自治区"));
        PROVINCES.add(new AreaItem(0, 650000, "新疆维吾尔自治区"));
        PROVINCES.add(new AreaItem(0, 710000, "台湾省"));
        PROVINCES.add(new AreaItem(0, 810000, "香港特别行政区"));
        PROVINCES.add(new AreaItem(0, 820000, "澳门特别行政区"));
    }

    void initCities() {
        //增加市
        CITIES.add(new AreaItem(110000, 110100, "市辖区"));
        CITIES.add(new AreaItem(110000, 110200, "县"));
        CITIES.add(new AreaItem(120000, 120100, "市辖区"));
        CITIES.add(new AreaItem(120000, 120200, "县"));
        CITIES.add(new AreaItem(130000, 130100, "石家庄市"));
        CITIES.add(new AreaItem(130000, 130200, "唐山市"));
        CITIES.add(new AreaItem(130000, 130300, "秦皇岛市"));
        CITIES.add(new AreaItem(130000, 130400, "邯郸市"));
        CITIES.add(new AreaItem(130000, 130500, "邢台市"));
        CITIES.add(new AreaItem(130000, 130600, "保定市"));
        CITIES.add(new AreaItem(130000, 130700, "张家口市"));
        CITIES.add(new AreaItem(130000, 130800, "承德市"));
        CITIES.add(new AreaItem(130000, 130900, "沧州市"));
        CITIES.add(new AreaItem(130000, 131000, "廊坊市"));
        CITIES.add(new AreaItem(130000, 131100, "衡水市"));
        CITIES.add(new AreaItem(140000, 140100, "太原市"));
        CITIES.add(new AreaItem(140000, 140200, "大同市"));
        CITIES.add(new AreaItem(140000, 140300, "阳泉市"));
        CITIES.add(new AreaItem(140000, 140400, "长治市"));
        CITIES.add(new AreaItem(140000, 140500, "晋城市"));
        CITIES.add(new AreaItem(140000, 140600, "朔州市"));
        CITIES.add(new AreaItem(140000, 140700, "晋中市"));
        CITIES.add(new AreaItem(140000, 140800, "运城市"));
        CITIES.add(new AreaItem(140000, 140900, "忻州市"));
        CITIES.add(new AreaItem(140000, 141000, "临汾市"));
        CITIES.add(new AreaItem(140000, 141100, "吕梁市"));
        CITIES.add(new AreaItem(150000, 150100, "呼和浩特市"));
        CITIES.add(new AreaItem(150000, 150200, "包头市"));
        CITIES.add(new AreaItem(150000, 150300, "乌海市"));
        CITIES.add(new AreaItem(150000, 150400, "赤峰市"));
        CITIES.add(new AreaItem(150000, 150500, "通辽市"));
        CITIES.add(new AreaItem(150000, 150600, "鄂尔多斯市"));
        CITIES.add(new AreaItem(150000, 150700, "呼伦贝尔市"));
        CITIES.add(new AreaItem(150000, 150800, "巴彦淖尔市"));
        CITIES.add(new AreaItem(150000, 150900, "乌兰察布市"));
        CITIES.add(new AreaItem(150000, 152200, "兴安盟"));
        CITIES.add(new AreaItem(150000, 152500, "锡林郭勒盟"));
        CITIES.add(new AreaItem(150000, 152900, "阿拉善盟"));
        CITIES.add(new AreaItem(210000, 210100, "沈阳市"));
        CITIES.add(new AreaItem(210000, 210200, "大连市"));
        CITIES.add(new AreaItem(210000, 210300, "鞍山市"));
        CITIES.add(new AreaItem(210000, 210400, "抚顺市"));
        CITIES.add(new AreaItem(210000, 210500, "本溪市"));
        CITIES.add(new AreaItem(210000, 210600, "丹东市"));
        CITIES.add(new AreaItem(210000, 210700, "锦州市"));
        CITIES.add(new AreaItem(210000, 210800, "营口市"));
        CITIES.add(new AreaItem(210000, 210900, "阜新市"));
        CITIES.add(new AreaItem(210000, 211000, "辽阳市"));
        CITIES.add(new AreaItem(210000, 211100, "盘锦市"));
        CITIES.add(new AreaItem(210000, 211200, "铁岭市"));
        CITIES.add(new AreaItem(210000, 211300, "朝阳市"));
        CITIES.add(new AreaItem(210000, 211400, "葫芦岛市"));
        CITIES.add(new AreaItem(220000, 220100, "长春市"));
        CITIES.add(new AreaItem(220000, 220200, "吉林市"));
        CITIES.add(new AreaItem(220000, 220300, "四平市"));
        CITIES.add(new AreaItem(220000, 220400, "辽源市"));
        CITIES.add(new AreaItem(220000, 220500, "通化市"));
        CITIES.add(new AreaItem(220000, 220600, "白山市"));
        CITIES.add(new AreaItem(220000, 220700, "松原市"));
        CITIES.add(new AreaItem(220000, 220800, "白城市"));
        CITIES.add(new AreaItem(220000, 222400, "延边朝鲜族自治州"));
        CITIES.add(new AreaItem(230000, 230100, "哈尔滨市"));
        CITIES.add(new AreaItem(230000, 230200, "齐齐哈尔市"));
        CITIES.add(new AreaItem(230000, 230300, "鸡西市"));
        CITIES.add(new AreaItem(230000, 230400, "鹤岗市"));
        CITIES.add(new AreaItem(230000, 230500, "双鸭山市"));
        CITIES.add(new AreaItem(230000, 230600, "大庆市"));
        CITIES.add(new AreaItem(230000, 230700, "伊春市"));
        CITIES.add(new AreaItem(230000, 230800, "佳木斯市"));
        CITIES.add(new AreaItem(230000, 230900, "七台河市"));
        CITIES.add(new AreaItem(230000, 231000, "牡丹江市"));
        CITIES.add(new AreaItem(230000, 231100, "黑河市"));
        CITIES.add(new AreaItem(230000, 231200, "绥化市"));
        CITIES.add(new AreaItem(230000, 232700, "大兴安岭地区"));
        CITIES.add(new AreaItem(310000, 310100, "市辖区"));
        CITIES.add(new AreaItem(310000, 310200, "县"));
        CITIES.add(new AreaItem(320000, 320100, "南京市"));
        CITIES.add(new AreaItem(320000, 320200, "无锡市"));
        CITIES.add(new AreaItem(320000, 320300, "徐州市"));
        CITIES.add(new AreaItem(320000, 320400, "常州市"));
        CITIES.add(new AreaItem(320000, 320500, "苏州市"));
        CITIES.add(new AreaItem(320000, 320600, "南通市"));
        CITIES.add(new AreaItem(320000, 320700, "连云港市"));
        CITIES.add(new AreaItem(320000, 320800, "淮安市"));
        CITIES.add(new AreaItem(320000, 320900, "盐城市"));
        CITIES.add(new AreaItem(320000, 321000, "扬州市"));
        CITIES.add(new AreaItem(320000, 321100, "镇江市"));
        CITIES.add(new AreaItem(320000, 321200, "泰州市"));
        CITIES.add(new AreaItem(320000, 321300, "宿迁市"));
        CITIES.add(new AreaItem(330000, 330100, "杭州市"));
        CITIES.add(new AreaItem(330000, 330200, "宁波市"));
        CITIES.add(new AreaItem(330000, 330300, "温州市"));
        CITIES.add(new AreaItem(330000, 330400, "嘉兴市"));
        CITIES.add(new AreaItem(330000, 330500, "湖州市"));
        CITIES.add(new AreaItem(330000, 330600, "绍兴市"));
        CITIES.add(new AreaItem(330000, 330700, "金华市"));
        CITIES.add(new AreaItem(330000, 330800, "衢州市"));
        CITIES.add(new AreaItem(330000, 330900, "舟山市"));
        CITIES.add(new AreaItem(330000, 331000, "台州市"));
        CITIES.add(new AreaItem(330000, 331100, "丽水市"));
        CITIES.add(new AreaItem(340000, 340100, "合肥市"));
        CITIES.add(new AreaItem(340000, 340200, "芜湖市"));
        CITIES.add(new AreaItem(340000, 340300, "蚌埠市"));
        CITIES.add(new AreaItem(340000, 340400, "淮南市"));
        CITIES.add(new AreaItem(340000, 340500, "马鞍山市"));
        CITIES.add(new AreaItem(340000, 340600, "淮北市"));
        CITIES.add(new AreaItem(340000, 340700, "铜陵市"));
        CITIES.add(new AreaItem(340000, 340800, "安庆市"));
        CITIES.add(new AreaItem(340000, 341000, "黄山市"));
        CITIES.add(new AreaItem(340000, 341100, "滁州市"));
        CITIES.add(new AreaItem(340000, 341200, "阜阳市"));
        CITIES.add(new AreaItem(340000, 341300, "宿州市"));
        CITIES.add(new AreaItem(340000, 341400, "巢湖市"));
        CITIES.add(new AreaItem(340000, 341500, "六安市"));
        CITIES.add(new AreaItem(340000, 341600, "亳州市"));
        CITIES.add(new AreaItem(340000, 341700, "池州市"));
        CITIES.add(new AreaItem(340000, 341800, "宣城市"));
        CITIES.add(new AreaItem(350000, 350100, "福州市"));
        CITIES.add(new AreaItem(350000, 350200, "厦门市"));
        CITIES.add(new AreaItem(350000, 350300, "莆田市"));
        CITIES.add(new AreaItem(350000, 350400, "三明市"));
        CITIES.add(new AreaItem(350000, 350500, "泉州市"));
        CITIES.add(new AreaItem(350000, 350600, "漳州市"));
        CITIES.add(new AreaItem(350000, 350700, "南平市"));
        CITIES.add(new AreaItem(350000, 350800, "龙岩市"));
        CITIES.add(new AreaItem(350000, 350900, "宁德市"));
        CITIES.add(new AreaItem(360000, 360100, "南昌市"));
        CITIES.add(new AreaItem(360000, 360200, "景德镇市"));
        CITIES.add(new AreaItem(360000, 360300, "萍乡市"));
        CITIES.add(new AreaItem(360000, 360400, "九江市"));
        CITIES.add(new AreaItem(360000, 360500, "新余市"));
        CITIES.add(new AreaItem(360000, 360600, "鹰潭市"));
        CITIES.add(new AreaItem(360000, 360700, "赣州市"));
        CITIES.add(new AreaItem(360000, 360800, "吉安市"));
        CITIES.add(new AreaItem(360000, 360900, "宜春市"));
        CITIES.add(new AreaItem(360000, 361000, "抚州市"));
        CITIES.add(new AreaItem(360000, 361100, "上饶市"));
        CITIES.add(new AreaItem(370000, 370100, "济南市"));
        CITIES.add(new AreaItem(370000, 370200, "青岛市"));
        CITIES.add(new AreaItem(370000, 370300, "淄博市"));
        CITIES.add(new AreaItem(370000, 370400, "枣庄市"));
        CITIES.add(new AreaItem(370000, 370500, "东营市"));
        CITIES.add(new AreaItem(370000, 370600, "烟台市"));
        CITIES.add(new AreaItem(370000, 370700, "潍坊市"));
        CITIES.add(new AreaItem(370000, 370800, "济宁市"));
        CITIES.add(new AreaItem(370000, 370900, "泰安市"));
        CITIES.add(new AreaItem(370000, 371000, "威海市"));
        CITIES.add(new AreaItem(370000, 371100, "日照市"));
        CITIES.add(new AreaItem(370000, 371200, "莱芜市"));
        CITIES.add(new AreaItem(370000, 371300, "临沂市"));
        CITIES.add(new AreaItem(370000, 371400, "德州市"));
        CITIES.add(new AreaItem(370000, 371500, "聊城市"));
        CITIES.add(new AreaItem(370000, 371600, "滨州市"));
        CITIES.add(new AreaItem(370000, 371700, "荷泽市"));
        CITIES.add(new AreaItem(410000, 410100, "郑州市"));
        CITIES.add(new AreaItem(410000, 410200, "开封市"));
        CITIES.add(new AreaItem(410000, 410300, "洛阳市"));
        CITIES.add(new AreaItem(410000, 410400, "平顶山市"));
        CITIES.add(new AreaItem(410000, 410500, "安阳市"));
        CITIES.add(new AreaItem(410000, 410600, "鹤壁市"));
        CITIES.add(new AreaItem(410000, 410700, "新乡市"));
        CITIES.add(new AreaItem(410000, 410800, "焦作市"));
        CITIES.add(new AreaItem(410000, 410900, "濮阳市"));
        CITIES.add(new AreaItem(410000, 411000, "许昌市"));
        CITIES.add(new AreaItem(410000, 411100, "漯河市"));
        CITIES.add(new AreaItem(410000, 411200, "三门峡市"));
        CITIES.add(new AreaItem(410000, 411300, "南阳市"));
        CITIES.add(new AreaItem(410000, 411400, "商丘市"));
        CITIES.add(new AreaItem(410000, 411500, "信阳市"));
        CITIES.add(new AreaItem(410000, 411600, "周口市"));
        CITIES.add(new AreaItem(410000, 411700, "驻马店市"));
        CITIES.add(new AreaItem(420000, 420100, "武汉市"));
        CITIES.add(new AreaItem(420000, 420200, "黄石市"));
        CITIES.add(new AreaItem(420000, 420300, "十堰市"));
        CITIES.add(new AreaItem(420000, 420500, "宜昌市"));
        CITIES.add(new AreaItem(420000, 420600, "襄樊市"));
        CITIES.add(new AreaItem(420000, 420700, "鄂州市"));
        CITIES.add(new AreaItem(420000, 420800, "荆门市"));
        CITIES.add(new AreaItem(420000, 420900, "孝感市"));
        CITIES.add(new AreaItem(420000, 421000, "荆州市"));
        CITIES.add(new AreaItem(420000, 421100, "黄冈市"));
        CITIES.add(new AreaItem(420000, 421200, "咸宁市"));
        CITIES.add(new AreaItem(420000, 421300, "随州市"));
        CITIES.add(new AreaItem(420000, 422800, "恩施土家族苗族自治州"));
        CITIES.add(new AreaItem(420000, 429000, "省直辖行政单位"));
        CITIES.add(new AreaItem(430000, 430100, "长沙市"));
        CITIES.add(new AreaItem(430000, 430200, "株洲市"));
        CITIES.add(new AreaItem(430000, 430300, "湘潭市"));
        CITIES.add(new AreaItem(430000, 430400, "衡阳市"));
        CITIES.add(new AreaItem(430000, 430500, "邵阳市"));
        CITIES.add(new AreaItem(430000, 430600, "岳阳市"));
        CITIES.add(new AreaItem(430000, 430700, "常德市"));
        CITIES.add(new AreaItem(430000, 430800, "张家界市"));
        CITIES.add(new AreaItem(430000, 430900, "益阳市"));
        CITIES.add(new AreaItem(430000, 431000, "郴州市"));
        CITIES.add(new AreaItem(430000, 431100, "永州市"));
        CITIES.add(new AreaItem(430000, 431200, "怀化市"));
        CITIES.add(new AreaItem(430000, 431300, "娄底市"));
        CITIES.add(new AreaItem(430000, 433100, "湘西土家族苗族自治州"));
        CITIES.add(new AreaItem(440000, 440100, "广州市"));
        CITIES.add(new AreaItem(440000, 440200, "韶关市"));
        CITIES.add(new AreaItem(440000, 440300, "深圳市"));
        CITIES.add(new AreaItem(440000, 440400, "珠海市"));
        CITIES.add(new AreaItem(440000, 440500, "汕头市"));
        CITIES.add(new AreaItem(440000, 440600, "佛山市"));
        CITIES.add(new AreaItem(440000, 440700, "江门市"));
        CITIES.add(new AreaItem(440000, 440800, "湛江市"));
        CITIES.add(new AreaItem(440000, 440900, "茂名市"));
        CITIES.add(new AreaItem(440000, 441200, "肇庆市"));
        CITIES.add(new AreaItem(440000, 441300, "惠州市"));
        CITIES.add(new AreaItem(440000, 441400, "梅州市"));
        CITIES.add(new AreaItem(440000, 441500, "汕尾市"));
        CITIES.add(new AreaItem(440000, 441600, "河源市"));
        CITIES.add(new AreaItem(440000, 441700, "阳江市"));
        CITIES.add(new AreaItem(440000, 441800, "清远市"));
        CITIES.add(new AreaItem(440000, 441900, "东莞市"));
        CITIES.add(new AreaItem(440000, 442000, "中山市"));
        CITIES.add(new AreaItem(440000, 445100, "潮州市"));
        CITIES.add(new AreaItem(440000, 445200, "揭阳市"));
        CITIES.add(new AreaItem(440000, 445300, "云浮市"));
        CITIES.add(new AreaItem(450000, 450100, "南宁市"));
        CITIES.add(new AreaItem(450000, 450200, "柳州市"));
        CITIES.add(new AreaItem(450000, 450300, "桂林市"));
        CITIES.add(new AreaItem(450000, 450400, "梧州市"));
        CITIES.add(new AreaItem(450000, 450500, "北海市"));
        CITIES.add(new AreaItem(450000, 450600, "防城港市"));
        CITIES.add(new AreaItem(450000, 450700, "钦州市"));
        CITIES.add(new AreaItem(450000, 450800, "贵港市"));
        CITIES.add(new AreaItem(450000, 450900, "玉林市"));
        CITIES.add(new AreaItem(450000, 451000, "百色市"));
        CITIES.add(new AreaItem(450000, 451100, "贺州市"));
        CITIES.add(new AreaItem(450000, 451200, "河池市"));
        CITIES.add(new AreaItem(450000, 451300, "来宾市"));
        CITIES.add(new AreaItem(450000, 451400, "崇左市"));
        CITIES.add(new AreaItem(460000, 460100, "海口市"));
        CITIES.add(new AreaItem(460000, 460200, "三亚市"));
        CITIES.add(new AreaItem(460000, 469000, "省直辖县级行政单位"));
        CITIES.add(new AreaItem(500000, 500100, "市辖区"));
        CITIES.add(new AreaItem(500000, 500200, "县"));
        CITIES.add(new AreaItem(500000, 500300, "市"));
        CITIES.add(new AreaItem(510000, 510100, "成都市"));
        CITIES.add(new AreaItem(510000, 510300, "自贡市"));
        CITIES.add(new AreaItem(510000, 510400, "攀枝花市"));
        CITIES.add(new AreaItem(510000, 510500, "泸州市"));
        CITIES.add(new AreaItem(510000, 510600, "德阳市"));
        CITIES.add(new AreaItem(510000, 510700, "绵阳市"));
        CITIES.add(new AreaItem(510000, 510800, "广元市"));
        CITIES.add(new AreaItem(510000, 510900, "遂宁市"));
        CITIES.add(new AreaItem(510000, 511000, "内江市"));
        CITIES.add(new AreaItem(510000, 511100, "乐山市"));
        CITIES.add(new AreaItem(510000, 511300, "南充市"));
        CITIES.add(new AreaItem(510000, 511400, "眉山市"));
        CITIES.add(new AreaItem(510000, 511500, "宜宾市"));
        CITIES.add(new AreaItem(510000, 511600, "广安市"));
        CITIES.add(new AreaItem(510000, 511700, "达州市"));
        CITIES.add(new AreaItem(510000, 511800, "雅安市"));
        CITIES.add(new AreaItem(510000, 511900, "巴中市"));
        CITIES.add(new AreaItem(510000, 512000, "资阳市"));
        CITIES.add(new AreaItem(510000, 513200, "阿坝藏族羌族自治州"));
        CITIES.add(new AreaItem(510000, 513300, "甘孜藏族自治州"));
        CITIES.add(new AreaItem(510000, 513400, "凉山彝族自治州"));
        CITIES.add(new AreaItem(520000, 520100, "贵阳市"));
        CITIES.add(new AreaItem(520000, 520200, "六盘水市"));
        CITIES.add(new AreaItem(520000, 520300, "遵义市"));
        CITIES.add(new AreaItem(520000, 520400, "安顺市"));
        CITIES.add(new AreaItem(520000, 522200, "铜仁地区"));
        CITIES.add(new AreaItem(520000, 522300, "黔西南布依族苗族自治州"));
        CITIES.add(new AreaItem(520000, 522400, "毕节地区"));
        CITIES.add(new AreaItem(520000, 522600, "黔东南苗族侗族自治州"));
        CITIES.add(new AreaItem(520000, 522700, "黔南布依族苗族自治州"));
        CITIES.add(new AreaItem(530000, 530100, "昆明市"));
        CITIES.add(new AreaItem(530000, 530300, "曲靖市"));
        CITIES.add(new AreaItem(530000, 530400, "玉溪市"));
        CITIES.add(new AreaItem(530000, 530500, "保山市"));
        CITIES.add(new AreaItem(530000, 530600, "昭通市"));
        CITIES.add(new AreaItem(530000, 530700, "丽江市"));
        CITIES.add(new AreaItem(530000, 530800, "思茅市"));
        CITIES.add(new AreaItem(530000, 530900, "临沧市"));
        CITIES.add(new AreaItem(530000, 532300, "楚雄彝族自治州"));
        CITIES.add(new AreaItem(530000, 532500, "红河哈尼族彝族自治州"));
        CITIES.add(new AreaItem(530000, 532600, "文山壮族苗族自治州"));
        CITIES.add(new AreaItem(530000, 532800, "西双版纳傣族自治州"));
        CITIES.add(new AreaItem(530000, 532900, "大理白族自治州"));
        CITIES.add(new AreaItem(530000, 533100, "德宏傣族景颇族自治州"));
        CITIES.add(new AreaItem(530000, 533300, "怒江傈僳族自治州"));
        CITIES.add(new AreaItem(530000, 533400, "迪庆藏族自治州"));
        CITIES.add(new AreaItem(540000, 540100, "拉萨市"));
        CITIES.add(new AreaItem(540000, 542100, "昌都地区"));
        CITIES.add(new AreaItem(540000, 542200, "山南地区"));
        CITIES.add(new AreaItem(540000, 542300, "日喀则地区"));
        CITIES.add(new AreaItem(540000, 542400, "那曲地区"));
        CITIES.add(new AreaItem(540000, 542500, "阿里地区"));
        CITIES.add(new AreaItem(540000, 542600, "林芝地区"));
        CITIES.add(new AreaItem(610000, 610100, "西安市"));
        CITIES.add(new AreaItem(610000, 610200, "铜川市"));
        CITIES.add(new AreaItem(610000, 610300, "宝鸡市"));
        CITIES.add(new AreaItem(610000, 610400, "咸阳市"));
        CITIES.add(new AreaItem(610000, 610500, "渭南市"));
        CITIES.add(new AreaItem(610000, 610600, "延安市"));
        CITIES.add(new AreaItem(610000, 610700, "汉中市"));
        CITIES.add(new AreaItem(610000, 610800, "榆林市"));
        CITIES.add(new AreaItem(610000, 610900, "安康市"));
        CITIES.add(new AreaItem(610000, 611000, "商洛市"));
        CITIES.add(new AreaItem(620000, 620100, "兰州市"));
        CITIES.add(new AreaItem(620000, 620200, "嘉峪关市"));
        CITIES.add(new AreaItem(620000, 620300, "金昌市"));
        CITIES.add(new AreaItem(620000, 620400, "白银市"));
        CITIES.add(new AreaItem(620000, 620500, "天水市"));
        CITIES.add(new AreaItem(620000, 620600, "武威市"));
        CITIES.add(new AreaItem(620000, 620700, "张掖市"));
        CITIES.add(new AreaItem(620000, 620800, "平凉市"));
        CITIES.add(new AreaItem(620000, 620900, "酒泉市"));
        CITIES.add(new AreaItem(620000, 621000, "庆阳市"));
        CITIES.add(new AreaItem(620000, 621100, "定西市"));
        CITIES.add(new AreaItem(620000, 621200, "陇南市"));
        CITIES.add(new AreaItem(620000, 622900, "临夏回族自治州"));
        CITIES.add(new AreaItem(620000, 623000, "甘南藏族自治州"));
        CITIES.add(new AreaItem(630000, 630100, "西宁市"));
        CITIES.add(new AreaItem(630000, 632100, "海东地区"));
        CITIES.add(new AreaItem(630000, 632200, "海北藏族自治州"));
        CITIES.add(new AreaItem(630000, 632300, "黄南藏族自治州"));
        CITIES.add(new AreaItem(630000, 632500, "海南藏族自治州"));
        CITIES.add(new AreaItem(630000, 632600, "果洛藏族自治州"));
        CITIES.add(new AreaItem(630000, 632700, "玉树藏族自治州"));
        CITIES.add(new AreaItem(630000, 632800, "海西蒙古族藏族自治州"));
        CITIES.add(new AreaItem(640000, 640100, "银川市"));
        CITIES.add(new AreaItem(640000, 640200, "石嘴山市"));
        CITIES.add(new AreaItem(640000, 640300, "吴忠市"));
        CITIES.add(new AreaItem(640000, 640400, "固原市"));
        CITIES.add(new AreaItem(640000, 640500, "中卫市"));
        CITIES.add(new AreaItem(650000, 650100, "乌鲁木齐市"));
        CITIES.add(new AreaItem(650000, 650200, "克拉玛依市"));
        CITIES.add(new AreaItem(650000, 652100, "吐鲁番地区"));
        CITIES.add(new AreaItem(650000, 652200, "哈密地区"));
        CITIES.add(new AreaItem(650000, 652300, "昌吉回族自治州"));
        CITIES.add(new AreaItem(650000, 652700, "博尔塔拉蒙古自治州"));
        CITIES.add(new AreaItem(650000, 652800, "巴音郭楞蒙古自治州"));
        CITIES.add(new AreaItem(650000, 652900, "阿克苏地区"));
        CITIES.add(new AreaItem(650000, 653000, "克孜勒苏柯尔克孜自治州"));
        CITIES.add(new AreaItem(650000, 653100, "喀什地区"));
        CITIES.add(new AreaItem(650000, 653200, "和田地区"));
        CITIES.add(new AreaItem(650000, 654000, "伊犁哈萨克自治州"));
        CITIES.add(new AreaItem(650000, 654200, "塔城地区"));
        CITIES.add(new AreaItem(650000, 654300, "阿勒泰地区"));
        CITIES.add(new AreaItem(650000, 659000, "省直辖行政单位"));

    }

    final List<AreaItem> PROVINCES = new ArrayList<>();
    final List<AreaItem> CITIES = new ArrayList<>();
    final List<AreaItem> AREAS = new ArrayList<>();

    /**
     * 返回省集合
     */
    public List<AreaItem> getProvinces() {
        if (PROVINCES == null || PROVINCES.size() == 0) {
            initProvinces();
        }
        return PROVINCES;
    }

    /**
     * 返回市集合
     */
    public List<AreaItem> getCities() {
        if (CITIES == null || CITIES.size() == 0) {
            initCities();
        }
        return CITIES;
    }

    /**
     * 返回地区集合
     */
    public List<AreaItem> getAreas() {
        if (AREAS == null || AREAS.size() == 0) {
            ChinaAreaPart1.Add(AREAS);
            ChinaAreaPart2.Add(AREAS);
        }
        return AREAS;
    }


}

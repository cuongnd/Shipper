package org.csware.ee.model;

import java.util.List;

/**
 * Created by Administrator on 2015/12/31.
 */
public class TrackListOrder extends Return {


    /**
     * Orders : [{"OwnerUser":{"Status":1,"CreateTime":"2015-09-06T17:51:49","CompanyStatus":-1,"Id":10013,"Mobile":"15371452845","Avatar":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/c211d17b-a9c0-466a-adc0-d57ea64d8271/original"},"GoodsType":"机械设备","Message":"","GoodsUnit":"公斤","ToDetail":"驾校H","Tax":"","TruckLength":1.8,"BearerRate":false,"ToTime":1451555013,"OwnerId":10013,"PriceCalType":"整车","PayPoint":0,"To":110101,"Bearer":{"VehicleLicense":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/59cdcea2-918e-48e7-ba56-e4ce6e4049c4/original","Rate":5,"VehicleType":"平板车","DrivingLicense":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/46337ba3-9282-4d60-a70c-f49bdd119d7a/original","VerifyMessage":"","Latitude":31.171125,"Longitude":121.40599,"Name":"李","Plate":"沪A889988","VehicleLoad":23,"VehiclePhoto":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/152300ec-cfff-4be9-8f45-e0213daa45ac/original","Score":998473,"VehicleLength":13,"UserId":10006,"OrderAmount":1},"FromTime":1451472208,"GoodsAmount":1,"PaymentStatus":0,"ToName":"","Status":3,"BearerId":10006,"BearerUser":{"Status":1,"CreateTime":"2015-07-31T15:58:45","CompanyStatus":-1,"Id":10006,"Mobile":"13381603257","Avatar":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/6cdf951f-fda6-43d4-857b-cb59a6ae8673/original"},"CreateTime":1451470259,"Images":"","From":310101,"BidsAmount":1,"OwnerRate":false,"ToNumber":"","DealPrice":1,"Price":0,"Insurance":"","TruckType":"面包车","PayMethod":2,"Id":21967,"FromDetail":"耀华路/上南路(路口)","PriceType":"","Track":{"LastTrackPoint":{"Latitude":31.171125,"Time":1451540800,"Longitude":121.40599},"LastUpdate":1451512000,"CreateTime":1451444905,"OrderId":21967,"Track":[{"Latitude":31.171125,"Time":1451540800,"Longitude":121.40599}]}},{"OwnerUser":{"Status":1,"CreateTime":"2015-09-06T17:51:49","CompanyStatus":-1,"Id":10013,"Mobile":"15371452845","Avatar":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/c211d17b-a9c0-466a-adc0-d57ea64d8271/original"},"GoodsType":"机械设备","Message":"","GoodsUnit":"公斤","ToDetail":"驾校H","Tax":"","TruckLength":1.8,"BearerRate":false,"ToTime":1451555013,"OwnerId":10013,"PriceCalType":"整车","PayPoint":0,"To":110101,"Bearer":{"VehicleLicense":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/c30cca79-6889-49dd-bd8b-8ce1f8b904e3/original","Rate":0,"VehicleType":"面包车","DrivingLicense":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/87fda327-5b61-4d85-8683-a06c83dfa91e/original","VerifyMessage":"","Latitude":31.17114,"Longitude":121.405999,"Name":"Yuui","Plate":"Uuiiii","VehicleLoad":20,"VehiclePhoto":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/8ca42d3f-0b80-427a-a5b5-9ea1df9de1ee/original","Score":1000475,"VehicleLength":1.8,"UserId":10004,"OrderAmount":0},"FromTime":1451472208,"GoodsAmount":1,"PaymentStatus":0,"ToName":"","Status":3,"BearerId":10004,"BearerUser":{"Status":1,"CreateTime":"2015-07-29T17:09:15","CompanyStatus":-1,"Id":10004,"Mobile":"18656315210","Avatar":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/fd6942ca-0efd-4486-b3c0-49a5b753a9c0/original"},"SubOrders":[{"GoodsType":"机械设备","Message":"","GoodsUnit":"公斤","ToDetail":"驾校H","Tax":"","TruckLength":1.8,"BearerRate":false,"ToTime":1451555013,"OwnerId":10013,"PriceCalType":"整车","PayPoint":0,"To":110101,"Bearer":{"VehicleLicense":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/59cdcea2-918e-48e7-ba56-e4ce6e4049c4/original","Rate":5,"VehicleType":"平板车","DrivingLicense":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/46337ba3-9282-4d60-a70c-f49bdd119d7a/original","VerifyMessage":"","Latitude":31.171125,"Longitude":121.40599,"Name":"李","Plate":"沪A889988","VehicleLoad":23,"VehiclePhoto":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/152300ec-cfff-4be9-8f45-e0213daa45ac/original","Score":998473,"VehicleLength":13,"UserId":10006,"OrderAmount":1},"FromTime":1451472208,"GoodsAmount":1,"PaymentStatus":0,"ToName":"","Status":3,"BearerId":10006,"BearerUser":{"Status":1,"CreateTime":"2015-07-31T15:58:45","CompanyStatus":-1,"Id":10006,"Mobile":"13381603257","Avatar":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/6cdf951f-fda6-43d4-857b-cb59a6ae8673/original"},"CreateTime":1451470259,"Images":"","From":310101,"BidsAmount":1,"OwnerRate":false,"ToNumber":"","DealPrice":1,"Price":0,"Insurance":"","TruckType":"面包车","PayMethod":2,"Id":21967,"FromDetail":"耀华路/上南路(路口)","PriceType":""}],"CreateTime":1451470259,"Images":"","From":310101,"BidsAmount":1,"OwnerRate":false,"ToNumber":"","DealPrice":1,"Price":0,"Insurance":"","TruckType":"面包车","PayMethod":2,"Id":21966,"FromDetail":"耀华路/上南路(路口)","PriceType":"","Track":{"LastTrackPoint":{"Latitude":31.17114,"Time":1451544936,"Longitude":121.405999},"LastUpdate":1451516136,"CreateTime":1451444696,"OrderId":21966,"Track":[{"Latitude":31.17114,"Time":1451544936,"Longitude":121.405999}]}},{"OwnerUser":{"Status":1,"CreateTime":"2015-09-06T17:51:49","CompanyStatus":-1,"Id":10013,"Mobile":"15371452845","Avatar":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/c211d17b-a9c0-466a-adc0-d57ea64d8271/original"},"GoodsType":"机械设备","Message":"","GoodsUnit":"公斤","ToDetail":"驾校H","Tax":"","TruckLength":1.8,"BearerRate":false,"ToTime":1451555013,"OwnerId":10013,"PriceCalType":"整车","PayPoint":0,"To":110101,"Bearer":{"VehicleLicense":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/ce3caadd-327f-4a6b-8897-e80722f822f6/original","Rate":0,"VehicleType":"中栏车","DrivingLicense":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/b54b731e-2901-4e7e-8c2a-be790c13ad7d/original","VerifyMessage":"","Latitude":31.171007,"Longitude":121.406005,"Name":"鹅鹅鹅","Plate":"沪A00000","VehicleLoad":20,"VehiclePhoto":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/ebdcd3a9-1771-41c8-9002-310d52f0ab9c/original","Score":1003240,"VehicleLength":6.2,"UserId":10028,"OrderAmount":0},"FromTime":1451472208,"GoodsAmount":1,"PaymentStatus":0,"ToName":"","Status":3,"BearerId":10028,"BearerUser":{"Status":1,"CreateTime":"2015-09-29T14:13:38","CompanyStatus":-1,"Id":10028,"Mobile":"18656315211","Avatar":""},"CreateTime":1451469181,"Images":"","From":310101,"BidsAmount":1,"OwnerRate":false,"ToNumber":"","DealPrice":0.1,"Price":0,"Insurance":"","TruckType":"面包车","PayMethod":2,"Id":21961,"FromDetail":"耀华路/上南路(路口)","PriceType":"","Track":{"LastTrackPoint":{"Latitude":31.171007,"Time":1451542190,"Longitude":121.406005},"LastUpdate":1451513390,"CreateTime":1451512400,"OrderId":21961,"Track":[{"Latitude":31.171007,"Time":1451542190,"Longitude":121.406005}]}},{"OwnerUser":{"Status":1,"CreateTime":"2015-09-06T17:51:49","CompanyStatus":-1,"Id":10013,"Mobile":"15371452845","Avatar":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/c211d17b-a9c0-466a-adc0-d57ea64d8271/original"},"GoodsType":"机械设备","Message":"","GoodsUnit":"公斤","ToDetail":"驾校H","Tax":"","TruckLength":1.8,"BearerRate":false,"ToTime":1451555013,"OwnerId":10013,"PriceCalType":"整车","PayPoint":0,"To":110101,"Bearer":{"VehicleLicense":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/c30cca79-6889-49dd-bd8b-8ce1f8b904e3/original","Rate":0,"VehicleType":"面包车","DrivingLicense":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/87fda327-5b61-4d85-8683-a06c83dfa91e/original","VerifyMessage":"","Latitude":31.17114,"Longitude":121.405999,"Name":"Yuui","Plate":"Uuiiii","VehicleLoad":20,"VehiclePhoto":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/8ca42d3f-0b80-427a-a5b5-9ea1df9de1ee/original","Score":1000475,"VehicleLength":1.8,"UserId":10004,"OrderAmount":0},"FromTime":1451472208,"GoodsAmount":1,"PaymentStatus":0,"ToName":"","Status":3,"BearerId":10004,"BearerUser":{"Status":1,"CreateTime":"2015-07-29T17:09:15","CompanyStatus":-1,"Id":10004,"Mobile":"18656315210","Avatar":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/fd6942ca-0efd-4486-b3c0-49a5b753a9c0/original"},"SubOrders":[{"GoodsType":"机械设备","Message":"","GoodsUnit":"公斤","ToDetail":"驾校H","Tax":"","TruckLength":1.8,"BearerRate":false,"ToTime":1451555013,"OwnerId":10013,"PriceCalType":"整车","PayPoint":0,"To":110101,"Bearer":{"VehicleLicense":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/ce3caadd-327f-4a6b-8897-e80722f822f6/original","Rate":0,"VehicleType":"中栏车","DrivingLicense":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/b54b731e-2901-4e7e-8c2a-be790c13ad7d/original","VerifyMessage":"","Latitude":31.171007,"Longitude":121.406005,"Name":"鹅鹅鹅","Plate":"沪A00000","VehicleLoad":20,"VehiclePhoto":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/ebdcd3a9-1771-41c8-9002-310d52f0ab9c/original","Score":1003240,"VehicleLength":6.2,"UserId":10028,"OrderAmount":0},"FromTime":1451472208,"GoodsAmount":1,"PaymentStatus":0,"ToName":"","Status":3,"BearerId":10028,"BearerUser":{"Status":1,"CreateTime":"2015-09-29T14:13:38","CompanyStatus":-1,"Id":10028,"Mobile":"18656315211","Avatar":""},"CreateTime":1451469181,"Images":"","From":310101,"BidsAmount":1,"OwnerRate":false,"ToNumber":"","DealPrice":0.1,"Price":0,"Insurance":"","TruckType":"面包车","PayMethod":2,"Id":21961,"FromDetail":"耀华路/上南路(路口)","PriceType":""}],"CreateTime":1451469181,"Images":"","From":310101,"BidsAmount":1,"OwnerRate":false,"ToNumber":"","DealPrice":0.1,"Price":0,"Insurance":"","TruckType":"面包车","PayMethod":2,"Id":21960,"FromDetail":"耀华路/上南路(路口)","PriceType":"","Track":{"LastTrackPoint":{"Latitude":31.17114,"Time":1451544936,"Longitude":121.405999},"LastUpdate":1451516136,"CreateTime":1451441662,"OrderId":21960,"Track":[{"Latitude":31.17114,"Time":1451544936,"Longitude":121.405999}]}},{"OwnerUser":{"Status":1,"CreateTime":"2015-09-06T17:51:49","CompanyStatus":-1,"Id":10013,"Mobile":"15371452845","Avatar":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/c211d17b-a9c0-466a-adc0-d57ea64d8271/original"},"GoodsType":"机械设备","Message":"","GoodsUnit":"公斤","ToDetail":"驾校H","Tax":"","TruckLength":1.8,"BearerRate":false,"ToTime":1451530834,"OwnerId":10013,"PriceCalType":"整车","PayPoint":0,"To":110101,"Bearer":{"VehicleLicense":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/c30cca79-6889-49dd-bd8b-8ce1f8b904e3/original","Rate":0,"VehicleType":"面包车","DrivingLicense":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/87fda327-5b61-4d85-8683-a06c83dfa91e/original","VerifyMessage":"","Latitude":31.17114,"Longitude":121.405999,"Name":"Yuui","Plate":"Uuiiii","VehicleLoad":20,"VehiclePhoto":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/8ca42d3f-0b80-427a-a5b5-9ea1df9de1ee/original","Score":1000475,"VehicleLength":1.8,"UserId":10004,"OrderAmount":0},"FromTime":1451448032,"GoodsAmount":50,"PaymentStatus":0,"ToName":"","Status":3,"BearerId":10004,"BearerUser":{"Status":1,"CreateTime":"2015-07-29T17:09:15","CompanyStatus":-1,"Id":10004,"Mobile":"18656315210","Avatar":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/fd6942ca-0efd-4486-b3c0-49a5b753a9c0/original"},"CreateTime":1451444660,"Images":"","From":310101,"BidsAmount":1,"OwnerRate":false,"ToNumber":"","DealPrice":0.01,"Price":0,"Insurance":"","TruckType":"面包车","PayMethod":2,"Id":21932,"FromDetail":"耀华路/上南路(路口)","PriceType":"","Track":{"LastTrackPoint":{"Latitude":31.17114,"Time":1451544936,"Longitude":121.405999},"LastUpdate":1451516136,"CreateTime":1451427419,"OrderId":21932,"Track":[{"Latitude":31.17114,"Time":1451544936,"Longitude":121.405999}]}},{"OwnerUser":{"Status":1,"CreateTime":"2015-09-06T17:51:49","CompanyStatus":-1,"Id":10013,"Mobile":"15371452845","Avatar":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/c211d17b-a9c0-466a-adc0-d57ea64d8271/original"},"GoodsType":"食品","Message":"","GoodsUnit":"方","ToDetail":"耀华路/上南路(路口)","Tax":"","TruckLength":1.8,"BearerRate":false,"ToTime":1447491763,"OwnerId":10013,"PriceCalType":"整车","PayPoint":0,"To":310101,"Bearer":{"VehicleLicense":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/c30cca79-6889-49dd-bd8b-8ce1f8b904e3/original","Rate":0,"VehicleType":"面包车","DrivingLicense":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/87fda327-5b61-4d85-8683-a06c83dfa91e/original","VerifyMessage":"","Latitude":31.17114,"Longitude":121.405999,"Name":"Yuui","Plate":"Uuiiii","VehicleLoad":20,"VehiclePhoto":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/8ca42d3f-0b80-427a-a5b5-9ea1df9de1ee/original","Score":1000475,"VehicleLength":1.8,"UserId":10004,"OrderAmount":0},"FromTime":1447326159,"GoodsAmount":12,"PaymentStatus":0,"ToName":"鹅鹅鹅","Status":5,"BearerId":10004,"BearerUser":{"Status":1,"CreateTime":"2015-07-29T17:09:15","CompanyStatus":-1,"Id":10004,"Mobile":"18656315210","Avatar":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/fd6942ca-0efd-4486-b3c0-49a5b753a9c0/original"},"CreateTime":1447318977,"Images":"","From":110101,"BidsAmount":1,"OwnerRate":false,"ToNumber":"18656315213","DealPrice":1,"Price":0,"Insurance":"","TruckType":"面包车","PayMethod":2,"Id":21113,"FromDetail":"麻辣焦点烤鱼吧(东直门店)","PriceType":"","Track":{"LastTrackPoint":{"Latitude":31.17114,"Time":1451544936,"Longitude":121.405999},"LastUpdate":1451516136,"CreateTime":1447294337,"OrderId":21113,"Track":[{"Latitude":31.17114,"Time":1451544936,"Longitude":121.405999}]}},{"OwnerUser":{"Status":1,"CreateTime":"2015-09-06T17:51:49","CompanyStatus":-1,"Id":10013,"Mobile":"15371452845","Avatar":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/c211d17b-a9c0-466a-adc0-d57ea64d8271/original"},"GoodsType":"食品","Message":"","GoodsUnit":"方","ToDetail":"","Tax":"","TruckLength":4.2,"BearerRate":false,"ToTime":1446884472,"OwnerId":10013,"PriceCalType":"整车","PayPoint":0,"To":0,"Bearer":{"VehicleLicense":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/c30cca79-6889-49dd-bd8b-8ce1f8b904e3/original","Rate":0,"VehicleType":"面包车","DrivingLicense":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/87fda327-5b61-4d85-8683-a06c83dfa91e/original","VerifyMessage":"","Latitude":31.17114,"Longitude":121.405999,"Name":"Yuui","Plate":"Uuiiii","VehicleLoad":20,"VehiclePhoto":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/8ca42d3f-0b80-427a-a5b5-9ea1df9de1ee/original","Score":1000475,"VehicleLength":1.8,"UserId":10004,"OrderAmount":0},"FromTime":1446798068,"GoodsAmount":1.1,"PaymentStatus":0,"ToName":"","Status":3,"BearerId":10004,"BearerUser":{"Status":1,"CreateTime":"2015-07-29T17:09:15","CompanyStatus":-1,"Id":10004,"Mobile":"18656315210","Avatar":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/fd6942ca-0efd-4486-b3c0-49a5b753a9c0/original"},"CreateTime":1446711694,"Images":"","From":310106,"BidsAmount":1,"OwnerRate":false,"ToNumber":"","DealPrice":0.01,"Price":0,"Insurance":"","TruckType":"高栏车","PayMethod":2,"Id":21051,"FromDetail":"华宁路2988号","PriceType":"","Track":{"LastTrackPoint":{"Latitude":31.17114,"Time":1451544936,"Longitude":121.405999},"LastUpdate":1451516136,"CreateTime":1447017544,"OrderId":21051,"Track":[{"Latitude":31.17114,"Time":1451544936,"Longitude":121.405999}]}},{"OwnerUser":{"Status":1,"CreateTime":"2015-09-06T17:51:49","CompanyStatus":-1,"Id":10013,"Mobile":"15371452845","Avatar":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/c211d17b-a9c0-466a-adc0-d57ea64d8271/original"},"GoodsType":"机械设备","Message":"","GoodsUnit":"方","ToDetail":"西兰","Tax":"","TruckLength":6.8,"BearerRate":false,"ToTime":1446002407,"OwnerId":10013,"PriceCalType":"整车","PayPoint":0,"To":110104,"Bearer":{"VehicleLicense":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/c30cca79-6889-49dd-bd8b-8ce1f8b904e3/original","Rate":0,"VehicleType":"面包车","DrivingLicense":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/87fda327-5b61-4d85-8683-a06c83dfa91e/original","VerifyMessage":"","Latitude":31.17114,"Longitude":121.405999,"Name":"Yuui","Plate":"Uuiiii","VehicleLoad":20,"VehiclePhoto":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/8ca42d3f-0b80-427a-a5b5-9ea1df9de1ee/original","Score":1000475,"VehicleLength":1.8,"UserId":10004,"OrderAmount":0},"FromTime":1445919604,"GoodsAmount":15,"PaymentStatus":0,"ToName":"贺爱兰","Status":5,"BearerId":10004,"BearerUser":{"Status":1,"CreateTime":"2015-07-29T17:09:15","CompanyStatus":-1,"Id":10004,"Mobile":"18656315210","Avatar":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/fd6942ca-0efd-4486-b3c0-49a5b753a9c0/original"},"CreateTime":1445916029,"Images":"","From":310106,"BidsAmount":1,"OwnerRate":false,"ToNumber":"15321452548","DealPrice":0.1,"Price":0,"Insurance":"","TruckType":"高栏车","PayMethod":2,"Id":20911,"FromDetail":"华宁路2988号","PriceType":"","Track":{"LastTrackPoint":{"Latitude":31.17114,"Time":1451544936,"Longitude":121.405999},"LastUpdate":1451516136,"CreateTime":1446163013,"OrderId":20911,"Track":[{"Latitude":31.17114,"Time":1451544936,"Longitude":121.405999}]}},{"OwnerUser":{"Status":1,"CreateTime":"2015-09-06T17:51:49","CompanyStatus":-1,"Id":10013,"Mobile":"15371452845","Avatar":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/c211d17b-a9c0-466a-adc0-d57ea64d8271/original"},"GoodsType":"机械设备","Message":"","GoodsUnit":"方","ToDetail":"西兰","Tax":"","TruckLength":6.2,"BearerRate":false,"ToTime":1446001757,"OwnerId":10013,"PriceCalType":"整车","PayPoint":0,"To":110104,"Bearer":{"VehicleLicense":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/c30cca79-6889-49dd-bd8b-8ce1f8b904e3/original","Rate":0,"VehicleType":"面包车","DrivingLicense":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/87fda327-5b61-4d85-8683-a06c83dfa91e/original","VerifyMessage":"","Latitude":31.17114,"Longitude":121.405999,"Name":"Yuui","Plate":"Uuiiii","VehicleLoad":20,"VehiclePhoto":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/8ca42d3f-0b80-427a-a5b5-9ea1df9de1ee/original","Score":1000475,"VehicleLength":1.8,"UserId":10004,"OrderAmount":0},"FromTime":1445918953,"GoodsAmount":15,"PaymentStatus":0,"ToName":"隔壁老王","Status":5,"BearerId":10004,"BearerUser":{"Status":1,"CreateTime":"2015-07-29T17:09:15","CompanyStatus":-1,"Id":10004,"Mobile":"18656315210","Avatar":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/fd6942ca-0efd-4486-b3c0-49a5b753a9c0/original"},"CreateTime":1445915506,"Images":"","From":310106,"BidsAmount":1,"OwnerRate":false,"ToNumber":"15312453654","DealPrice":0.1,"Price":0,"Insurance":"","TruckType":"低栏车","PayMethod":2,"Id":20910,"FromDetail":"华宁路2988号","PriceType":"","Track":{"LastTrackPoint":{"Latitude":31.17114,"Time":1451544936,"Longitude":121.405999},"LastUpdate":1451516136,"CreateTime":1446163013,"OrderId":20910,"Track":[{"Latitude":31.17114,"Time":1451544936,"Longitude":121.405999}]}},{"OwnerUser":{"Status":1,"CreateTime":"2015-09-06T17:51:49","CompanyStatus":-1,"Id":10013,"Mobile":"15371452845","Avatar":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/c211d17b-a9c0-466a-adc0-d57ea64d8271/original"},"GoodsType":"建材","Message":"","GoodsUnit":"吨","ToDetail":"325号","Tax":"普通发票","TruckLength":7.2,"BearerRate":false,"ToTime":1448552361,"OwnerId":10013,"PriceCalType":"零担","PayPoint":1,"To":130101,"Bearer":{"VehicleLicense":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/ce3caadd-327f-4a6b-8897-e80722f822f6/original","Rate":0,"VehicleType":"中栏车","DrivingLicense":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/b54b731e-2901-4e7e-8c2a-be790c13ad7d/original","VerifyMessage":"","Latitude":31.171007,"Longitude":121.406005,"Name":"鹅鹅鹅","Plate":"沪A00000","VehicleLoad":20,"VehiclePhoto":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/ebdcd3a9-1771-41c8-9002-310d52f0ab9c/original","Score":1003240,"VehicleLength":6.2,"UserId":10028,"OrderAmount":0},"FromTime":1445956753,"GoodsAmount":1,"PaymentStatus":0,"ToName":"老王","Status":3,"BearerId":10028,"BearerUser":{"Status":1,"CreateTime":"2015-09-29T14:13:38","CompanyStatus":-1,"Id":10028,"Mobile":"18656315211","Avatar":""},"CreateTime":1443507699,"Images":"","From":110101,"BidsAmount":1,"OwnerRate":false,"ToNumber":"15371452845","DealPrice":1,"Price":1,"Insurance":"","TruckType":"中栏车","PayMethod":2,"Id":20534,"FromDetail":"16736","PriceType":"元/吨","Track":{"LastTrackPoint":{"Latitude":31.171007,"Time":1451542190,"Longitude":121.406005},"LastUpdate":1451513390,"CreateTime":1450832937,"OrderId":20534,"Track":[{"Latitude":31.171007,"Time":1451542190,"Longitude":121.406005}]}}]
     */
    public List<OrdersEntity> Orders;

    public static class OrdersEntity {
        /**
         * OwnerUser : {"Status":1,"CreateTime":"2015-09-06T17:51:49","CompanyStatus":-1,"Id":10013,"Mobile":"15371452845","Avatar":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/c211d17b-a9c0-466a-adc0-d57ea64d8271/original"}
         * GoodsType : 机械设备
         * Message :
         * GoodsUnit : 公斤
         * ToDetail : 驾校H
         * Tax :
         * TruckLength : 1.8
         * BearerRate : false
         * ToTime : 1451555013
         * OwnerId : 10013
         * PriceCalType : 整车
         * PayPoint : 0
         * To : 110101
         * Bearer : {"VehicleLicense":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/59cdcea2-918e-48e7-ba56-e4ce6e4049c4/original","Rate":5,"VehicleType":"平板车","DrivingLicense":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/46337ba3-9282-4d60-a70c-f49bdd119d7a/original","VerifyMessage":"","Latitude":31.171125,"Longitude":121.40599,"Name":"李","Plate":"沪A889988","VehicleLoad":23,"VehiclePhoto":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/152300ec-cfff-4be9-8f45-e0213daa45ac/original","Score":998473,"VehicleLength":13,"UserId":10006,"OrderAmount":1}
         * FromTime : 1451472208
         * GoodsAmount : 1.0
         * PaymentStatus : 0
         * ToName :
         * Status : 3
         * BearerId : 10006
         * BearerUser : {"Status":1,"CreateTime":"2015-07-31T15:58:45","CompanyStatus":-1,"Id":10006,"Mobile":"13381603257","Avatar":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/6cdf951f-fda6-43d4-857b-cb59a6ae8673/original"}
         * CreateTime : 1451470259
         * Images :
         * From : 310101
         * BidsAmount : 1
         * OwnerRate : false
         * ToNumber :
         * DealPrice : 1.0
         * Price : 0.0
         * Insurance :
         * TruckType : 面包车
         * PayMethod : 2
         * Id : 21967
         * FromDetail : 耀华路/上南路(路口)
         * PriceType :
         * Track : {"LastTrackPoint":{"Latitude":31.171125,"Time":1451540800,"Longitude":121.40599},"LastUpdate":1451512000,"CreateTime":1451444905,"OrderId":21967,"Track":[{"Latitude":31.171125,"Time":1451540800,"Longitude":121.40599}]}
         */
        public OwnerUserEntity OwnerUser;
        public String GoodsType;
        public String Message;
        public String GoodsUnit;
        public String ToDetail;
        public String Tax;
        public double TruckLength;
        public boolean BearerRate;
        public int ToTime;
        public int OwnerId;
        public String PriceCalType;
        public int PayPoint;
        public int To;
        public BearerEntity Bearer;
        public int FromTime;
        public double GoodsAmount;
        public int PaymentStatus;
        public String ToName;
        public int Status;
        public int BearerId;
        public BearerUserEntity BearerUser;
        public int CreateTime;
        public String Images;
        public int From;
        public int BidsAmount;
        public boolean OwnerRate;
        public String ToNumber;
        public double DealPrice;
        public double Price;
        public String Insurance;
        public String TruckType;
        public int PayMethod;
        public int Id;
        public String FromDetail;
        public String PriceType;
        public TrackEntity Track;
        public BearerCompanyEntity BearerCompany;
        public List<PayeeEntity> Payee;
        public List<SubOrdersEntity> SubOrders;
        public static class SubOrdersEntity {
            /**
             * GoodsType : 机械设备
             * Message :
             * GoodsUnit : 公斤
             * ToDetail : 驾校H
             * Tax :
             * TruckLength : 1.8
             * BearerRate : false
             * ToTime : 1451555013
             * OwnerId : 10013
             * PriceCalType : 整车
             * PayPoint : 0
             * To : 110101
             * Bearer : {"VehicleLicense":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/59cdcea2-918e-48e7-ba56-e4ce6e4049c4/original","Rate":5,"VehicleType":"平板车","DrivingLicense":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/46337ba3-9282-4d60-a70c-f49bdd119d7a/original","VerifyMessage":"","Latitude":37.791485,"Longitude":121.40561,"Name":"李","Plate":"沪A889988","VehicleLoad":23,"VehiclePhoto":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/152300ec-cfff-4be9-8f45-e0213daa45ac/original","Score":998463,"VehicleLength":13,"UserId":10006,"OrderAmount":1}
             * FromTime : 1451472208
             * GoodsAmount : 1.0
             * PaymentStatus : 0
             * ToName :
             * Status : 3
             * BearerId : 10006
             * BearerUser : {"Status":1,"CreateTime":"2015-07-31T15:58:45","CompanyStatus":-1,"Id":10006,"Mobile":"13381603257","Avatar":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/6cdf951f-fda6-43d4-857b-cb59a6ae8673/original"}
             * CreateTime : 1451470259
             * Images :
             * From : 310101
             * BidsAmount : 1
             * OwnerRate : false
             * ToNumber :
             * DealPrice : 1.0
             * Price : 0.0
             * Insurance :
             * TruckType : 面包车
             * PayMethod : 2
             * Id : 21967
             * FromDetail : 耀华路/上南路(路口)
             * PriceType :
             */
            public String GoodsType;
            public String Message;
            public String GoodsUnit;
            public String ToDetail;
            public String Tax;
            public double TruckLength;
            public boolean BearerRate;
            public int ToTime;
            public int OwnerId;
            public String PriceCalType;
            public int PayPoint;
            public int To;
            public BearerEntity Bearer;
            public int FromTime;
            public double GoodsAmount;
            public int PaymentStatus;
            public String ToName;
            public int Status;
            public int BearerId;
            public BearerUserEntity BearerUser;
            public int CreateTime;
            public String Images;
            public int From;
            public int BidsAmount;
            public boolean OwnerRate;
            public String ToNumber;
            public double DealPrice;
            public double Price;
            public String Insurance;
            public String TruckType;
            public int PayMethod;
            public int Id;
            public String FromDetail;
            public String PriceType;

            public static class BearerEntity {
                /**
                 * VehicleLicense : http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/59cdcea2-918e-48e7-ba56-e4ce6e4049c4/original
                 * Rate : 5.0
                 * VehicleType : 平板车
                 * DrivingLicense : http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/46337ba3-9282-4d60-a70c-f49bdd119d7a/original
                 * VerifyMessage :
                 * Latitude : 37.791485
                 * Longitude : 121.40561
                 * Name : 李
                 * Plate : 沪A889988
                 * VehicleLoad : 23.0
                 * VehiclePhoto : http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/152300ec-cfff-4be9-8f45-e0213daa45ac/original
                 * Score : 998463
                 * VehicleLength : 13.0
                 * UserId : 10006
                 * OrderAmount : 1
                 */
                public String VehicleLicense;
                public double Rate;
                public String VehicleType;
                public String DrivingLicense;
                public String VerifyMessage;
                public double Latitude;
                public double Longitude;
                public String Name;
                public String Plate;
                public double VehicleLoad;
                public String VehiclePhoto;
                public int Score;
                public double VehicleLength;
                public int UserId;
                public int OrderAmount;
            }

            public static class BearerUserEntity {
                /**
                 * Status : 1
                 * CreateTime : 2015-07-31T15:58:45
                 * CompanyStatus : -1
                 * Id : 10006
                 * Mobile : 13381603257
                 * Avatar : http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/6cdf951f-fda6-43d4-857b-cb59a6ae8673/original
                 */
                public int Status;
                public String CreateTime;
                public int CompanyStatus;
                public int Id;
                public String Mobile;
                public String Avatar;
            }
        }
        public static class PayeeEntity{
            /**
             *  "Id": 54,
             "OrderId": 21051,
             "Price": 0.0100,
             "Status": 1,
             "TransactionId": "",
             "Name": "隔壁老王",
             "Mobile": "18653554555",
             "Area": 110104,
             "Address": "西兰",
             "BearerBonus": 0.0000,
             "OwnerBonus": 0.0000,
             "PayerBonus": 0.0000
             * */
            public int Id;
            public int OrderId;
            public double Price;
            public double BearerBonus;
            public double PayerBonus;
            public double OwnerBonus;
            public int Status;
            public String TransactionId;
            public String Name;
            public String Mobile;
            public String Address;
            public int Area;
        }
        public static class OwnerUserEntity {
            /**
             * Status : 1
             * CreateTime : 2015-09-06T17:51:49
             * CompanyStatus : -1
             * Id : 10013
             * Mobile : 15371452845
             * Avatar : http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/c211d17b-a9c0-466a-adc0-d57ea64d8271/original
             */
            public int Status;
            public String CreateTime;
            public int CompanyStatus;
            public int Id;
            public String Mobile;
            public String Avatar;
        }

        public static class BearerEntity {
            /**
             * VehicleLicense : http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/59cdcea2-918e-48e7-ba56-e4ce6e4049c4/original
             * Rate : 5.0
             * VehicleType : 平板车
             * DrivingLicense : http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/46337ba3-9282-4d60-a70c-f49bdd119d7a/original
             * VerifyMessage :
             * Latitude : 31.171125
             * Longitude : 121.40599
             * Name : 李
             * Plate : 沪A889988
             * VehicleLoad : 23.0
             * VehiclePhoto : http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/152300ec-cfff-4be9-8f45-e0213daa45ac/original
             * Score : 998473
             * VehicleLength : 13.0
             * UserId : 10006
             * OrderAmount : 1
             */
            public String VehicleLicense;
            public double Rate;
            public String VehicleType;
            public String DrivingLicense;
            public String VerifyMessage;
            public double Latitude;
            public double Longitude;
            public String Name;
            public String Plate;
            public double VehicleLoad;
            public String VehiclePhoto;
            public int Score;
            public double VehicleLength;
            public int UserId;
            public int OrderAmount;
        }

        public static class BearerUserEntity {
            /**
             * Status : 1
             * CreateTime : 2015-07-31T15:58:45
             * CompanyStatus : -1
             * Id : 10006
             * Mobile : 13381603257
             * Avatar : http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/6cdf951f-fda6-43d4-857b-cb59a6ae8673/original
             */
            public int Status;
            public String CreateTime;
            public int CompanyStatus;
            public int Id;
            public String Mobile;
            public String Avatar;
        }

        public static class BearerCompanyEntity{
            /**
             *  UserId: 10004,
             *  LegalPerson: 得到,
             *  LegalPersonId: 342530199501054512,
             *  LegalPersonFront: http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/6cfea772-7ad1-4dfc-aba8-7af98d11ff38/original,
             *  LegalPersonBack: http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/68d80d33-406c-4fe6-92b2-4465211a6756/original,
             *  CompanyName: 分,
             *  BusinessLicence : http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/30450e70-6664-4207-ba79-be4d034fe29d/original
             */
            public int UserId;
            public String LegalPerson;
            public String LegalPersonId;
            public String LegalPersonFront;
            public String LegalPersonBack;
            public String BusinessLicence;
            public String CompanyName;
        }

        public static class TrackEntity {
            /**
             * LastTrackPoint : {"Latitude":31.171125,"Time":1451540800,"Longitude":121.40599}
             * LastUpdate : 1451512000
             * CreateTime : 1451444905
             * OrderId : 21967
             * Track : [{"Latitude":31.171125,"Time":1451540800,"Longitude":121.40599}]
             */
            public LastTrackPointEntity LastTrackPoint;
            public int LastUpdate;
            public int CreateTime;
            public int OrderId;
            public List<TrackChildEntity> Track;

            public static class LastTrackPointEntity {
                /**
                 * Latitude : 31.171125
                 * Time : 1451540800
                 * Longitude : 121.40599
                 */
                public double Latitude;
                public int Time;
                public double Longitude;
            }

            public static class TrackChildEntity {
                /**
                 * Latitude : 31.171125
                 * Time : 1451540800
                 * Longitude : 121.40599
                 */
                public double Latitude;
                public int Time;
                public double Longitude;
            }
        }
    }
}

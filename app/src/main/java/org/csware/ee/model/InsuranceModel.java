package org.csware.ee.model;

import java.util.List;

/**
 * Created by Administrator on 2015/9/22.
 */
public class InsuranceModel extends Return {

    /**
     * InsuranceTypes : [{"IsLeaf":false,"Content":"家具家居","Value":false,"Children":[{"IsLeaf":false,"Content":"有包装","Value":false,"Children":[{"IsLeaf":true,"Content":"木制品","Value":true,"Children":[],"Id":"10101","IsVisible":true},{"IsLeaf":true,"Content":"软木制品","Value":true,"Children":[],"Id":"10102","IsVisible":true},{"IsLeaf":true,"Content":"陶瓷制品","Value":true,"Children":[],"Id":"10103","IsVisible":true},{"IsLeaf":true,"Content":"玻璃及其制品似材料的制品（免赔额10%）","Value":true,"Children":[],"Id":"10104","IsVisible":true},{"IsLeaf":true,"Content":"石料、石膏、水泥、石棉、云母及类似材料制品","Value":true,"Children":[],"Id":"10105","IsVisible":true},{"IsLeaf":true,"Content":"纸、纸板、及其制品","Value":true,"Children":[],"Id":"10106","IsVisible":true}],"Id":"101","IsVisible":true},{"IsLeaf":true,"Content":"无包装","Value":false,"Children":[],"Id":"102","IsVisible":true}],"Id":"1","IsVisible":true},{"IsLeaf":false,"Content":"化工","Value":false,"Children":[{"IsLeaf":false,"Content":"有包装","Value":false,"Children":[{"IsLeaf":true,"Content":"塑料及其制品","Value":true,"Children":[],"Id":"20101","IsVisible":true},{"IsLeaf":true,"Content":"橡胶及其制品","Value":true,"Children":[],"Id":"20102","IsVisible":true},{"IsLeaf":true,"Content":"化学工业及其相关工业的产品（装化学品的压力容器不保；危险化学品不保）","Value":true,"Children":[],"Id":"20103","IsVisible":true}],"Id":"201","IsVisible":true},{"IsLeaf":true,"Content":"无包装","Value":false,"Children":[],"Id":"202","IsVisible":true}],"Id":"2","IsVisible":true},{"IsLeaf":false,"Content":"木材","Value":false,"Children":[{"IsLeaf":false,"Content":"有包装","Value":false,"Children":[{"IsLeaf":true,"Content":"木及其制品","Value":true,"Children":[],"Id":"30101","IsVisible":true},{"IsLeaf":true,"Content":"木炭","Value":true,"Children":[],"Id":"30102","IsVisible":true},{"IsLeaf":true,"Content":"软木及软木制品","Value":true,"Children":[],"Id":"30103","IsVisible":true},{"IsLeaf":true,"Content":"木浆及其他纤维状纤维素浆","Value":true,"Children":[],"Id":"30104","IsVisible":true},{"IsLeaf":true,"Content":"回收（废碎）纸或纸杯","Value":true,"Children":[],"Id":"30105","IsVisible":true}],"Id":"301","IsVisible":true},{"IsLeaf":true,"Content":"无包装","Value":false,"Children":[],"Id":"302","IsVisible":true}],"Id":"3","IsVisible":true},{"IsLeaf":false,"Content":"日用品","Value":false,"Children":[{"IsLeaf":false,"Content":"有包装","Value":false,"Children":[{"IsLeaf":true,"Content":"鞋、帽、伞、杖、鞭及其零件","Value":true,"Children":[],"Id":"40101","IsVisible":true},{"IsLeaf":true,"Content":"皮革、毛皮及其制品","Value":true,"Children":[],"Id":"40102","IsVisible":true},{"IsLeaf":true,"Content":"鞍具及挽具","Value":true,"Children":[],"Id":"40103","IsVisible":true},{"IsLeaf":true,"Content":"旅行制品、手提包及其类似容器、动物肠线（蚕胶丝除外）制品","Value":true,"Children":[],"Id":"40104","IsVisible":true}],"Id":"401","IsVisible":true},{"IsLeaf":true,"Content":"无包装","Value":false,"Children":[],"Id":"402","IsVisible":true}],"Id":"4","IsVisible":true},{"IsLeaf":false,"Content":"食品","Value":false,"Children":[{"IsLeaf":false,"Content":"有包装","Value":false,"Children":[{"IsLeaf":true,"Content":"饮料、酒、醋","Value":true,"Children":[],"Id":"50101","IsVisible":true},{"IsLeaf":true,"Content":"烟草及烟草代用品的制品（免陪额10%）","Value":true,"Children":[],"Id":"50102","IsVisible":true},{"IsLeaf":true,"Content":"动植物油、脂及其分产品","Value":true,"Children":[],"Id":"50103","IsVisible":true},{"IsLeaf":true,"Content":"精制的食用油脂","Value":true,"Children":[],"Id":"50104","IsVisible":true},{"IsLeaf":true,"Content":"动植物蜡","Value":true,"Children":[],"Id":"50105","IsVisible":true}],"Id":"501","IsVisible":true},{"IsLeaf":true,"Content":"无包装","Value":false,"Children":[],"Id":"502","IsVisible":true}],"Id":"5","IsVisible":true},{"IsLeaf":false,"Content":"矿产","Value":false,"Children":[{"IsLeaf":true,"Content":"有包装","Value":true,"Children":[],"Id":"601","IsVisible":true},{"IsLeaf":true,"Content":"无包装","Value":false,"Children":[],"Id":"602","IsVisible":true}],"Id":"6","IsVisible":true}]
     */
    private List<InsuranceTypesEntity> InsuranceTypes;

    public void setInsuranceTypes(List<InsuranceTypesEntity> InsuranceTypes) {
        this.InsuranceTypes = InsuranceTypes;
    }

    public List<InsuranceTypesEntity> getInsuranceTypes() {
        return InsuranceTypes;
    }

    public static class InsuranceTypesEntity {
        /**
         * IsLeaf : false
         * Content : 家具家居
         * Value : false
         * Children : [{"IsLeaf":false,"Content":"有包装","Value":false,"Children":[{"IsLeaf":true,"Content":"木制品","Value":true,"Children":[],"Id":"10101","IsVisible":true},{"IsLeaf":true,"Content":"软木制品","Value":true,"Children":[],"Id":"10102","IsVisible":true},{"IsLeaf":true,"Content":"陶瓷制品","Value":true,"Children":[],"Id":"10103","IsVisible":true},{"IsLeaf":true,"Content":"玻璃及其制品似材料的制品（免赔额10%）","Value":true,"Children":[],"Id":"10104","IsVisible":true},{"IsLeaf":true,"Content":"石料、石膏、水泥、石棉、云母及类似材料制品","Value":true,"Children":[],"Id":"10105","IsVisible":true},{"IsLeaf":true,"Content":"纸、纸板、及其制品","Value":true,"Children":[],"Id":"10106","IsVisible":true}],"Id":"101","IsVisible":true},{"IsLeaf":true,"Content":"无包装","Value":false,"Children":[],"Id":"102","IsVisible":true}]
         * Id : 1
         * IsVisible : true
         */
        private boolean IsLeaf;
        private String Content;
        private boolean Value;
        private List<ChildrenEntity> Children;
        private String Id;
        private boolean IsVisible;

        public void setIsLeaf(boolean IsLeaf) {
            this.IsLeaf = IsLeaf;
        }

        public void setContent(String Content) {
            this.Content = Content;
        }

        public void setValue(boolean Value) {
            this.Value = Value;
        }

        public void setChildren(List<ChildrenEntity> Children) {
            this.Children = Children;
        }

        public void setId(String Id) {
            this.Id = Id;
        }

        public void setIsVisible(boolean IsVisible) {
            this.IsVisible = IsVisible;
        }

        public boolean isIsLeaf() {
            return IsLeaf;
        }

        public String getContent() {
            return Content;
        }

        public boolean isValue() {
            return Value;
        }

        public List<ChildrenEntity> getChildren() {
            return Children;
        }

        public String getId() {
            return Id;
        }

        public boolean isIsVisible() {
            return IsVisible;
        }

        public static class ChildrenEntity {
            /**
             * IsLeaf : false
             * Content : 有包装
             * Value : false
             * Children : [{"IsLeaf":true,"Content":"木制品","Value":true,"Children":[],"Id":"10101","IsVisible":true},{"IsLeaf":true,"Content":"软木制品","Value":true,"Children":[],"Id":"10102","IsVisible":true},{"IsLeaf":true,"Content":"陶瓷制品","Value":true,"Children":[],"Id":"10103","IsVisible":true},{"IsLeaf":true,"Content":"玻璃及其制品似材料的制品（免赔额10%）","Value":true,"Children":[],"Id":"10104","IsVisible":true},{"IsLeaf":true,"Content":"石料、石膏、水泥、石棉、云母及类似材料制品","Value":true,"Children":[],"Id":"10105","IsVisible":true},{"IsLeaf":true,"Content":"纸、纸板、及其制品","Value":true,"Children":[],"Id":"10106","IsVisible":true}]
             * Id : 101
             * IsVisible : true
             */
            private boolean IsLeaf;
            private String Content;
            private boolean Value;
            private List<ChildrenChEntity> Children;
            private String Id;
            private boolean IsVisible;

            public void setIsLeaf(boolean IsLeaf) {
                this.IsLeaf = IsLeaf;
            }

            public void setContent(String Content) {
                this.Content = Content;
            }

            public void setValue(boolean Value) {
                this.Value = Value;
            }

            public void setChildren(List<ChildrenChEntity> Children) {
                this.Children = Children;
            }

            public void setId(String Id) {
                this.Id = Id;
            }

            public void setIsVisible(boolean IsVisible) {
                this.IsVisible = IsVisible;
            }

            public boolean isIsLeaf() {
                return IsLeaf;
            }

            public String getContent() {
                return Content;
            }

            public boolean isValue() {
                return Value;
            }

            public List<ChildrenChEntity> getChildren() {
                return Children;
            }

            public String getId() {
                return Id;
            }

            public boolean isIsVisible() {
                return IsVisible;
            }

            public static class ChildrenChEntity {
                /**
                 * IsLeaf : true
                 * Content : 木制品
                 * Value : true
                 * Children : []
                 * Id : 10101
                 * IsVisible : true
                 */
                private boolean IsLeaf;
                private String Content;
                private boolean Value;
                private List<?> Children;
                private String Id;
                private boolean IsVisible;

                public void setIsLeaf(boolean IsLeaf) {
                    this.IsLeaf = IsLeaf;
                }

                public void setContent(String Content) {
                    this.Content = Content;
                }

                public void setValue(boolean Value) {
                    this.Value = Value;
                }

                public void setChildren(List<?> Children) {
                    this.Children = Children;
                }

                public void setId(String Id) {
                    this.Id = Id;
                }

                public void setIsVisible(boolean IsVisible) {
                    this.IsVisible = IsVisible;
                }

                public boolean isIsLeaf() {
                    return IsLeaf;
                }

                public String getContent() {
                    return Content;
                }

                public boolean isValue() {
                    return Value;
                }

                public List<?> getChildren() {
                    return Children;
                }

                public String getId() {
                    return Id;
                }

                public boolean isIsVisible() {
                    return IsVisible;
                }
            }
        }
    }
}

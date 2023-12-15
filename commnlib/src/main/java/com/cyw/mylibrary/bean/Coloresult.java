package com.cyw.mylibrary.bean;

import java.io.Serializable;
import java.util.List;

/**
 * author： cyw
 */
public class Coloresult implements Serializable {
    /**
     * AcutalQuantity : {"InnerColorCode":"TEST","ColorantCode":[{"content":"V"},{"content":"K"},{"content":"L"}],"ActualQua":[{"content":"0.65"},{"content":"0.65"},{"content":"0.65"}],"ControlCode":"1"}
     */

    private AcutalQuantityBean AcutalQuantity;

    public AcutalQuantityBean getAcutalQuantity() {
        return AcutalQuantity;
    }

    public void setAcutalQuantity(AcutalQuantityBean acutalQuantity) {
        AcutalQuantity = acutalQuantity;
    }

    public static class AcutalQuantityBean implements Serializable {
        /**
         * InnerColorCode : TEST
         * ColorantCode : [{"content":"V"},{"content":"K"},{"content":"L"}]
         * ActualQua : [{"content":"0.65"},{"content":"0.65"},{"content":"0.65"}]
         * ControlCode : 1
         */
        private String ControlCode;
        private String InnerColorCode;
        private List<ColorantCodeBean> colorantCodeBean;
       // private List<ActualQuaBean> ActualQua;

        public String getInnerColorCode() {
            return InnerColorCode;
        }

        public void setInnerColorCode(String innerColorCode) {
            InnerColorCode = innerColorCode;
        }

        public String getControlCode() {
            return ControlCode;
        }

        public void setControlCode(String controlCode) {
            ControlCode = controlCode;
        }

        public List<ColorantCodeBean> getColorantCodeBean() {
            return colorantCodeBean;
        }

        public void setColorantCodeBean(List<ColorantCodeBean> colorantCodeBean) {
            this.colorantCodeBean = colorantCodeBean;
        }

        public static class ColorantCodeBean implements Serializable {
            /**
             * content : V
             */

            private String ColorantCode;
            private String ActualQua;

            public String getColorantCode() {
                return ColorantCode;
            }

            public void setColorantCode(String colorantCode) {
                ColorantCode = colorantCode;
            }

            public String getActualQua() {
                return ActualQua;
            }

            public void setActualQua(String actualQua) {
                ActualQua = actualQua;
            }
        }

//        public static class ActualQuaBean implements Serializable {
//            /**
//             * content : 0.65
//             */
//
//            private String content;
//
//            public String getContent() {
//                return content;
//            }
//
//            public void setContent(String content) {
//                this.content = content;
//            }
//        }
    }
//     private String ControlCode;//0代表调色失败 1代表调色成功
//    private String InnerColorCode;
//    private String ColorantCode;
//    private String ActualQua;
////    private String ColorantCode;
////    <ActualQua>0.65</ActualQua>
////    <ColorantCode>L</ColorantCode>
////    <ActualQua>0.65</ActualQua>

}

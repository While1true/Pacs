package coms.pacs.pacs.Model;

import java.util.List;

/**
 * Created by 不听话的好孩子 on 2018/2/8.
 */

public class Test {

    /**
     * code : 12
     * list : [{"sss":2},{"xx":"2s"},{"ss":{"z":5}}]
     */

    private int code;
    private List<ListBean> list;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * sss : 2
         * xx : 2s
         * ss : {"z":5}
         */

        private int sss;
        private String xx;
        private SsBean ss;

        public int getSss() {
            return sss;
        }

        public void setSss(int sss) {
            this.sss = sss;
        }

        public String getXx() {
            return xx;
        }

        public void setXx(String xx) {
            this.xx = xx;
        }

        public SsBean getSs() {
            return ss;
        }

        public void setSs(SsBean ss) {
            this.ss = ss;
        }

        public static class SsBean {
            /**
             * z : 5
             */

            private int z;

            public int getZ() {
                return z;
            }

            public void setZ(int z) {
                this.z = z;
            }
        }
    }
}

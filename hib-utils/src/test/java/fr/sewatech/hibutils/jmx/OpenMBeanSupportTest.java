package fr.sewatech.hibutils.jmx;

import org.junit.Test;

import javax.management.openmbean.CompositeData;
import javax.management.openmbean.InvalidKeyException;
import javax.management.openmbean.TabularData;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class OpenMBeanSupportTest {

    private final OpenMBeanSupport openMBeanSupport = new OpenMBeanSupport();

    @Test
    public void test_toCompositeData() throws Exception {
        Source source = new Source();
        CompositeData compositeData = openMBeanSupport.toCompositeData(source, "NAME");

        assertThat(compositeData.get("attrLong")).isEqualTo(source.attrLong);
        assertThat(compositeData.get("attrBoolean")).isEqualTo(source.attrBoolean);
        assertThat(compositeData.get("attrString")).isEqualTo(source.attrString);
        assertThat(compositeData.get("attrStringArray")).isEqualTo(source.attrStringArray);
        assertThatThrownBy(() -> compositeData.get("attrObject")).isInstanceOf(InvalidKeyException.class);
    }

    @Test
    public void test_toTabularData() throws Exception {
        TabularData tabularData = openMBeanSupport.toTabularData(
                name -> openMBeanSupport.toCompositeData(new Source(), name),
                new String[]{"SRC1", "SRC2", "SRC3"},
                "NAME"
        );

        assertThat(tabularData.containsKey(new String[]{"SRC1"})).isTrue();
        assertThat(tabularData.containsKey(new String[]{"XXX"})).isFalse();
        assertThat(tabularData.size()).isEqualTo(3);
    }

    private static class Source {
        private long attrLong = 42L;
        private boolean attrBoolean = false;
        private String attrString = "For tea too";
        private String[] attrStringArray = {"ONE", "TWO", "TREE"};
        private Object attrObject = new Object();

        public long getAttrLong() {
            return attrLong;
        }

        public Object getAttrBoolean() {
            return attrBoolean;
        }

        public String getAttrString() {
            return attrString;
        }

        public String[] getAttrStringArray() {
            return attrStringArray;
        }

        public Object getAttrObject() {
            return attrObject;
        }

    }

}
package Objects;

import java.util.ArrayList;

public class IndicatorDetails {
    public String tagname;
    public String indicatortype;
    public ArrayList<String> dependencies;
    public ArrayList<Double> params;
    public String primetype;
    public String extra;

    public IndicatorDetails(String tagname, String indicatortype, ArrayList<String> dependencies, ArrayList<Double> params, String primetype, String extra) {
        this.tagname = tagname;
        this.indicatortype = indicatortype;
        this.dependencies = dependencies;
        this.params = params;
        this.primetype = primetype;
        this.extra = extra;
    }

    public String getTagname() {
        return tagname;
    }

    public void setTagname(String tagname) {
        this.tagname = tagname;
    }

    public String getIndicatortype() {
        return indicatortype;
    }

    public void setIndicatortype(String indicatortype) {
        this.indicatortype = indicatortype;
    }

    public ArrayList<String> getDependencies() {
        return dependencies;
    }

    public void setDependencies(ArrayList<String> dependencies) {
        this.dependencies = dependencies;
    }

    public ArrayList<Double> getParams() {
        return params;
    }

    public void setParams(ArrayList<Double> params) {
        this.params = params;
    }

    public String getPrimetype() {
        return primetype;
    }

    public void setPrimetype(String primetype) {
        this.primetype = primetype;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package browser.termallod.core.html;

/**
 *
 * @author elahi
 */
public class HtmlParameters {

    private final Boolean textFileModifyFlag;
    private final Boolean listOfTemPageFlag;
    private final Boolean termPageFlag;
    private final Boolean alternativeFlag;

    public HtmlParameters(Boolean textFileModifyFlag, Boolean listOfTemPageFlag, Boolean termPageFlag, Boolean alternativeFlag) {
        this.textFileModifyFlag = textFileModifyFlag;
        this.listOfTemPageFlag = listOfTemPageFlag;
        this.termPageFlag = termPageFlag;
        this.alternativeFlag = alternativeFlag;
    }

    public Boolean getTextFileModifyFlag() {
        return textFileModifyFlag;
    }

    public Boolean getListOfTemPageFlag() {
        return listOfTemPageFlag;
    }

    public Boolean getTermPageFlag() {
        return termPageFlag;
    }

    public Boolean getAlternativeFlag() {
        return alternativeFlag;
    }

}

package org.example.page;

import org.example.methods.BaseSteps;

public class HomePage extends BaseSteps {

    public void searchProductAndGoList(){
        waitBySecond(2);
        clickElement("searchBoxforClick");
        sendKeysWithCsv("searchBoxforSendKeys");
        sendKeyToElementENTER("searchBoxforSendKeys");
        waitBySecond(3);
        swipePage();
    }
}

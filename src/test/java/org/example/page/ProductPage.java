package org.example.page;

import org.example.methods.BaseSteps;

public class ProductPage extends BaseSteps {

    public void productPageApplications(){
        javascriptclicker("selectIPhone");
        swipePage();
        printProductMoney("phonePrice");
        clickElement("goToStore");
    }
}

package com.example.datacorrects.view;

/**
 *
 *
 */

public interface PumpSetListener {
    void onModelClick(String model);
    void onMaterialClick(String materialType);
    void onPumpTypeClick(String pumpType);
    void onUnitClick(int type, String unit);
}

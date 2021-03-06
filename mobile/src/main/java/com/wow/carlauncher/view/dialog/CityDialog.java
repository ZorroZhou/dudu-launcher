package com.wow.carlauncher.view.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.wow.carlauncher.R;
import com.wow.carlauncher.common.view.wheelWidget.OnWheelChangedListener;
import com.wow.carlauncher.common.view.wheelWidget.WheelView;
import com.wow.carlauncher.common.view.wheelWidget.adapters.ArrayWheelAdapter;
import com.wow.carlauncher.repertory.city.XmlParserHandler;
import com.wow.carlauncher.repertory.city.model.CityModel;
import com.wow.carlauncher.repertory.city.model.DistrictModel;
import com.wow.carlauncher.repertory.city.model.ProvinceModel;
import com.wow.carlauncher.view.base.BaseDialog2;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by 10124 on 2017/11/15.
 */

public class CityDialog extends BaseDialog2 implements OnWheelChangedListener {
    private static String[] mProvinceDatas;
    private static Map<String, String[]> mCitisDatasMap = new HashMap<>();
    private static Map<String, String[]> mDistrictDatasMap = new HashMap<>();
    private static String dCurrentProviceName;
    private static String dCurrentCityName;
    private static String dCurrentDistrictName = "";


    private String mCurrentProviceName;
    private String mCurrentCityName = "";
    private String mCurrentDistrictName = "";

    public String getCityName() {
        return mCurrentCityName;
    }

    public String getDistrictName() {
        return mCurrentDistrictName;
    }

    private WheelView mViewProvince;
    private WheelView mViewCity;
    private WheelView mViewDistrict;

    public CityDialog(Context context) {
        super(context);
        setTitle("选择省市区");
        setView(LayoutInflater.from(getContext()).inflate(R.layout.dialog_city, new LinearLayout(context), true));
        setOnShowListenerEx(new OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                initProvinceDatas();
                mCurrentProviceName = dCurrentProviceName;
                mCurrentCityName = dCurrentCityName;
                mCurrentDistrictName = dCurrentDistrictName;
                setUpViews();
                setUpListener();
                setUpData();
            }
        });
    }

    private void setUpViews() {
        mViewProvince = findViewById(R.id.id_province);
        mViewCity = findViewById(R.id.id_city);
        mViewDistrict = findViewById(R.id.id_district);
    }

    private void setUpListener() {
        // 添加change事件
        mViewProvince.addChangingListener(this);
        // 添加change事件
        mViewCity.addChangingListener(this);
        // 添加change事件
        mViewDistrict.addChangingListener(this);
    }

    private void setUpData() {
        initProvinceDatas();
        mViewProvince.setViewAdapter(new ArrayWheelAdapter<>(getContext(), mProvinceDatas));
        mViewProvince.setCurrentItem(3);
        // 设置可见条目数量
        mViewProvince.setVisibleItems(7);
        mViewCity.setVisibleItems(7);
        mViewDistrict.setVisibleItems(7);
        updateCities();
        updateAreas();
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        if (wheel == mViewProvince) {
            updateCities();
        } else if (wheel == mViewCity) {
            updateAreas();
        } else if (wheel == mViewDistrict) {
            mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[newValue];
        }
    }

    private void updateAreas() {
        int pCurrent = mViewCity.getCurrentItem();
        mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];
        String[] areas = mDistrictDatasMap.get(mCurrentCityName);

        if (areas == null) {
            areas = new String[]{""};
        }
        if (areas.length > 0) {
            mCurrentDistrictName = areas[0];
        }
        mViewDistrict.setViewAdapter(new ArrayWheelAdapter<>(getContext(), areas));
        mViewDistrict.setCurrentItem(0);
    }

    /**
     * 根据当前的省，更新市WheelView的信息
     */
    private void updateCities() {
        int pCurrent = mViewProvince.getCurrentItem();
        mCurrentProviceName = mProvinceDatas[pCurrent];
        String[] cities = mCitisDatasMap.get(mCurrentProviceName);
        if (cities == null) {
            cities = new String[]{""};
        }
        if (cities.length > 0) {
            mCurrentCityName = cities[0];
        }
        mViewCity.setViewAdapter(new ArrayWheelAdapter<>(getContext(), cities));
        mViewCity.setCurrentItem(0);
        updateAreas();
    }


    private void initProvinceDatas() {
        if (mProvinceDatas != null) {
            return;
        }
        List<ProvinceModel> provinceList = null;
        AssetManager asset = getContext().getAssets();
        try {
            InputStream input = asset.open("city_data.xml");
            // 创建一个解析xml的工厂对象
            SAXParserFactory spf = SAXParserFactory.newInstance();
            // 解析xml
            SAXParser parser = spf.newSAXParser();
            XmlParserHandler handler = new XmlParserHandler();
            parser.parse(input, handler);
            input.close();
            // 获取解析出来的数据
            provinceList = handler.getDataList();
            //*/ 初始化默认选中的省、市、区
            if (provinceList != null && !provinceList.isEmpty()) {
                dCurrentProviceName = provinceList.get(0).getName();
                List<CityModel> cityList = provinceList.get(0).getCityList();
                if (cityList != null && !cityList.isEmpty()) {
                    dCurrentCityName = cityList.get(0).getName();
                    List<DistrictModel> districtList = cityList.get(0).getDistrictList();
                    dCurrentDistrictName = districtList.get(0).getName();
                }
            }
            //*/
            mProvinceDatas = new String[provinceList.size()];
            for (int i = 0; i < provinceList.size(); i++) {
                // 遍历所有省的数据
                mProvinceDatas[i] = provinceList.get(i).getName();
                List<CityModel> cityList = provinceList.get(i).getCityList();
                String[] cityNames = new String[cityList.size()];
                for (int j = 0; j < cityList.size(); j++) {
                    // 遍历省下面的所有市的数据
                    cityNames[j] = cityList.get(j).getName();
                    List<DistrictModel> districtList = cityList.get(j).getDistrictList();
                    String[] distrinctNameArray = new String[districtList.size()];
                    DistrictModel[] distrinctArray = new DistrictModel[districtList.size()];
                    for (int k = 0; k < districtList.size(); k++) {
                        // 遍历市下面所有区/县的数据
                        DistrictModel districtModel = new DistrictModel(districtList.get(k).getName(), districtList.get(k).getZipcode());
                        // 区/县对于的邮编，保存到mZipcodeDatasMap
                        distrinctArray[k] = districtModel;
                        distrinctNameArray[k] = districtModel.getName();
                    }
                    // 市-区/县的数据，保存到mDistrictDatasMap
                    mDistrictDatasMap.put(cityNames[j], distrinctNameArray);
                }
                // 省-市的数据，保存到mCitisDatasMap
                mCitisDatasMap.put(provinceList.get(i).getName(), cityNames);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {

        }
    }
}

package com.gdxx.crawler.grab;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gdxx.crawler.constant.Url;
import com.gdxx.crawler.constant.Xpath;
import com.gdxx.crawler.pojo.LineDataCombine;
import com.gdxx.crawler.pojo.LineNode;
import com.gdxx.dao.entity.LineClassify;
import com.gdxx.dao.entity.LineMajor;
import com.gdxx.dao.entity.LineStation;
import com.gdxx.dao.mapper.LineClassifyMapper;
import com.gdxx.dao.mapper.LineMajorMapper;
import com.gdxx.dao.mapper.LineStationMapper;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import lombok.extern.slf4j.Slf4j;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;


/**
 * @program: grab
 * @author: lulif
 * @create: 2021-02-04 13:54
 * @description:
 **/
@Slf4j
@Component
public class CompleteGrab implements CommandLineRunner {

    @Autowired
    private LineMajorMapper lineMajorMapper;

    @Autowired
    private LineClassifyMapper lineClassifyMapper;

    @Autowired
    private LineStationMapper lineStationMapper;

    @Autowired
    private LatLngGrab latLngGrab;

    /**
     * 线路首字母种类
     */
    private List<String> linesTypeList = Lists.newArrayList();
    /**
     * 首字母-Dom节点List(用于跳转url获取详情信息)
     */
    private Multimap<String, LineNode> type2LineMultiMap = ArrayListMultimap.create();
    /**
     * 已落库的线路
     */
    private List<String> hasGrabLineName = Lists.newArrayList();

    /**
     * 获取线路首字母分类
     *
     * @throws Exception
     */
    private void doLinesTypeGet() throws Exception {
        String url = Url.LINES_TYPE_URL;
        String content = Jsoup.connect(url).get().html();
        HtmlCleaner hc = new HtmlCleaner();
        TagNode tn = hc.clean(content);
        String xPath = Xpath.LINES_TYPE_XPATH;
        Object[] objects = tn.evaluateXPath(xPath);
        for (Object object : objects) {
            TagNode node = (TagNode) object;
            List<TagNode> childNodeList = node.getChildTagList();
            childNodeList.forEach(childNode -> linesTypeList.add(childNode.getText().toString()));
        }
        log.info("已获取各线路分类信息");
    }

    /**
     * 获取线路信息
     *
     * @throws Exception
     */
    private void doLinesGet() throws Exception {
        for (String lineType : linesTypeList) {
            String url = Url.LINES_URL + lineType;
            String content = Jsoup.connect(url).get().html();
            HtmlCleaner hc = new HtmlCleaner();
            TagNode tn = hc.clean(content);
            String xPath = Xpath.LINES_XPATH;
            Object[] objects = tn.evaluateXPath(xPath);
            for (Object object : objects) {
                TagNode node = (TagNode) object;
                List<TagNode> childNodeList = node.getChildTagList();
                childNodeList.forEach(childNode -> {
                    LineNode lineNode = new LineNode();
                    Map<String, String> attrMap = childNode.getAttributes();
                    String name = childNode.getText().toString();
                    lineNode.setHref(attrMap.get("href"));
                    lineNode.setName(name);
                    type2LineMultiMap.put(lineType, lineNode);
                });
            }
        }
        log.info("已获取各线路节点信息");
    }

    /**
     * 获取线路详情信息
     *
     * @param lineNode
     * @return
     * @throws Exception
     */
    private LineDataCombine doLineDetailGet(LineNode lineNode) throws Exception {
        String url = Url.LINE_DETAIL_URL + lineNode.getHref();
        String content = Jsoup.connect(url).get().html();
        HtmlCleaner hc = new HtmlCleaner();
        TagNode tn = hc.clean(content);

        LineDataCombine dataCombine = new LineDataCombine();
        LineMajor lineMajor = new LineMajor();
        lineMajor.setName(lineNode.getName());
        dataCombine.setLineMajor(lineMajor);

        doLineDetailTimeGet(tn, dataCombine);
        log.info("---doLineDetailTimeGet---done---");

        doLineDetailFrequencyGet(tn, dataCombine);
        log.info("---doLineDetailFrequencyGet---done---");

        doLineDetailSectionGet(tn, dataCombine);
        log.info("---doLineDetailSectionGet---done---");

        doLineDetailStationGet(tn, dataCombine);
        log.info("---doLineDetailStationGet---done---");

        return dataCombine;
    }

    private void doLineDetailStationGet(TagNode tn, LineDataCombine dataCombine) throws Exception {
        String xPath = Xpath.LINE_DETAIL_STATION_XPATH;
        Object[] objects = tn.evaluateXPath(xPath);
        Multimap<Integer, LineStation> classify2StationMap = ArrayListMultimap.create();
        for (int x = 0; x < objects.length; x++) {
            TagNode node = (TagNode) objects[x];
            List<TagNode> childNodeList = node.getChildTagList();
            for (TagNode tagNode : childNodeList) {
                List<TagNode> stationNodeList = tagNode.getChildTagList();
                String stationName = "";
                for (int i = 0; i < stationNodeList.size(); i++) {
                    LineStation lineStation = new LineStation();
                    stationName = stationNodeList.get(i).getText().toString();
                    lineStation.setName(stationName);
                    classify2StationMap.put(Integer.valueOf(x), lineStation);
                }
            }
            List<LineClassify> classifyList = dataCombine.getLineClassifyList();
            for (int y = 0; y < classifyList.size(); y++) {
                classifyList.get(y).setStationNum(classify2StationMap.get(y).size());
            }
        }
        dataCombine.setClassify2StationMap(classify2StationMap);
    }


    private void doLineDetailSectionGet(TagNode tn, LineDataCombine dataCombine) throws Exception {
        String xPath = Xpath.LINE_DETAIL_SECTION_XPATH;
        Object[] objects = tn.evaluateXPath(xPath);
        List<LineClassify> classifyList = dataCombine.getLineClassifyList();
        for (Object object : objects) {
            TagNode node = (TagNode) object;
            String section = node.getText().toString().trim();
            for (LineClassify lineClassify : classifyList) {
                if (Objects.isNull(lineClassify.getSection())) {
                    lineClassify.setSection(section);
                    break;
                }
            }
        }
        int num = objects.length;
        Iterator<LineClassify> iterable = classifyList.iterator();
        while (iterable.hasNext()) {
            iterable.next();
            --num;
            if (num < 0) {
                iterable.remove();
            }
        }
        System.out.println();
    }

    /**
     * 获取 发车信息
     *
     * @param tn
     * @param dataCombine
     * @throws Exception
     */
    private void doLineDetailFrequencyGet(TagNode tn, LineDataCombine dataCombine) throws Exception {
        String xPath = Xpath.LINE_DETAIL_FREQUENCY_XPATH;
        Object[] objects = tn.evaluateXPath(xPath);
        for (Object object : objects) {
            TagNode node = (TagNode) object;
            LineMajor lineMajor = dataCombine.getLineMajor();
            String frequencyDesc = node.getText().toString().trim();
            lineMajor.setFrequencyDesc(frequencyDesc);
            String[] strArr = frequencyDesc.trim().split("\n");
            boolean findOrNot = false;
            for (int k = 0; k < strArr.length; k++) {
                boolean fla4WholeTime = strArr[k].contains("单程") || strArr[k].contains("单圈");
                boolean flag4Frequency = strArr[k].contains("间隔") || strArr[k].contains("定时发车");
                if (!findOrNot && (fla4WholeTime || flag4Frequency)) {
                    String[] frequencyArr = strArr[k].trim().split(",");
                    for (int i = 0; i < frequencyArr.length; i++) {
                        if (frequencyArr[i].contains("单程") || frequencyArr[i].contains("单圈")) {
                            String wholeTime = frequencyArr[i].substring(2, frequencyArr[i].length() - 1);
                            lineMajor.setWholeTime(wholeTime);
                        }
                        if (frequencyArr[i].contains("间隔")) {
                            Integer index = frequencyArr[i].indexOf("间隔");
                            String frequency = frequencyArr[i].substring(index + 2, frequencyArr[i].length() - 1);
                            lineMajor.setFrequency(frequency);
                        }
                        if (frequencyArr[i].contains("定时发车")) {
                            lineMajor.setFrequency("定时发车");
                        }
                        findOrNot = true;
                    }
                }
            }
        }
    }


    /**
     * 获取 运行时间,票价,公交公司,最后更新时间
     *
     * @param tn
     * @param dataCombine
     * @throws Exception
     */
    private void doLineDetailTimeGet(TagNode tn, LineDataCombine dataCombine) throws Exception {
        String xPath = Xpath.LINE_DETAIL_TIME_XPATH;
        Object[] objects = tn.evaluateXPath(xPath);
        for (Object object : objects) {
            TagNode node = (TagNode) object;
            List<TagNode> childNodeList = node.getChildTagList();
            childNodeList.forEach(childNode -> {
                LineMajor lineMajor = dataCombine.getLineMajor();
                String str = childNode.getText().toString();
                String content = str.substring(5);
                //特殊处理
                if (Objects.equals(lineMajor.getName(), "497路")) {
                    content = "太平车站5:40-18:00|瓶窑汽车站6:00-18:40/19:30";
                }
                if (str.contains("运行时间")) {
                    dataCombine.getLineMajor().setRunTimeDesc(content);
                    String[] contentArr = content.split("\\|");
                    List<LineClassify> classifyList = Lists.newArrayList();
                    for (int i = 0; i < contentArr.length; i++) {
                        LineClassify lineClassify = new LineClassify();
                        classifyList.add(lineClassify);
                        int numIndex = getStrNumIndex(contentArr[i]);
                        if (numIndex > 0) {
                            String timeStr = contentArr[i].substring(numIndex);
                            String[] timeStrArr = timeStr.split("-");
                            if (timeStrArr.length <= 1) {
                                timeStrArr = timeStr.split("\\/");
                            }
                            if (timeStrArr.length >= 1) {
                                lineClassify.setFirstBusTime(timeStrArr[0]);
                                if (timeStrArr.length >= 2) {
                                    lineClassify.setLastBusTime(timeStrArr[1]);
                                }
                            }
                        }
                    }
                    dataCombine.setLineClassifyList(classifyList);
                }
                if (str.contains("参考票价")) {
                    String[] contentArr = content.split(",");
                    if (contentArr.length > 0) {
                        String cost;
                        if (contentArr[0].contains("免费")) {
                            cost = "0";
                        } else {
                            cost = contentArr[0].substring(0, contentArr[0].length() - 1);
                        }
                        lineMajor.setPrice(cost);
                    }
                }
                if (str.contains("公交公司")) {
                    lineMajor.setCompany(content);
                }
                if (str.contains("最后更新")) {
                    lineMajor.setLastUpdateTime(content.substring(0, 10));
                }
            });
        }
    }

    @Transactional
    public void writeDB(LineDataCombine dataCombine) {

        LineMajor lineMajor = dataCombine.getLineMajor();
        if (hasGrabLineName.contains(lineMajor.getName())) {
            return;
        }
        lineMajorMapper.insert(lineMajor);

        List<LineClassify> classifyList = dataCombine.getLineClassifyList();
        classifyList.forEach(item -> item.setMajorId(lineMajor.getId()));
        if (!CollectionUtils.isEmpty(classifyList)) {
            lineClassifyMapper.batchInsert(classifyList);
            Map<Integer, Collection<LineStation>> map = dataCombine.getClassify2StationMap().asMap();
            for (Map.Entry<Integer, Collection<LineStation>> entry : map.entrySet()) {
                List<LineStation> list = new ArrayList(entry.getValue());
                Long classifyId = classifyList.get(entry.getKey()).getId();
                list.forEach(item -> {
                    item.setClassifyId(classifyId);
                    item.setMajorId(lineMajor.getId());
                });
                if (!CollectionUtils.isEmpty(list)) {
                    lineStationMapper.batchInsert(list);
                }
            }
        }
        hasGrabLineName.add(lineMajor.getName());
        log.info("{}数据写入数据库", lineMajor.getName());
    }


    /**
     * 获取字符串中第一个字符的下标
     *
     * @param s
     * @return
     */
    private int getStrNumIndex(String s) {
        for (int i = 0; i < s.length(); i++) {
            if (Character.isDigit(s.charAt(i))) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void run(String... args) {
//        //测试开关
//        linesTypeList.add("4");
//        LineNode node = new LineNode();
//        node.setName("497路");
//        node.setHref("/x_eb7477a7");
//        type2LineMultiMap.put("4", node);

        try {
            doLinesTypeGet();
            doLinesGet();
        } catch (Exception e) {
            log.error("线路分类及节点信息获取失败{}", e.getMessage());
        }

        List<LineStation> hasGrabLineList = lineStationMapper.selectList(Wrappers.lambdaQuery());
        hasGrabLineName = hasGrabLineList.stream().map(LineStation::getName).collect(Collectors.toList());

        Map<String, Collection<LineNode>> map = type2LineMultiMap.asMap();
        for (Map.Entry<String, Collection<LineNode>> entry : map.entrySet()) {
            List<LineNode> list = new ArrayList<>(entry.getValue());
            for (LineNode lineNode : list) {
                if (lineNode.getName().contains("地铁")) {
                    continue;
                }
                LineDataCombine dataCombine = null;
                try {
                    dataCombine = doLineDetailGet(lineNode);
                } catch (Exception e) {
                    log.error("{}获取线路详情信息失败:{}", lineNode.getName(), e.getMessage());
                }
                try {
                    if (Objects.nonNull(dataCombine)) {
                        writeDB(dataCombine);
                    } else {
                        log.error("{} dataCombine为null", lineNode.getName());
                    }
                } catch (Exception e) {
                    log.error("{}写库失败:{}", lineNode.getName(), e.getMessage());
                }
            }
        }
        log.info("------------公交数据落库完毕----------");

        latLngGrab.stationLatLngGet();


        log.info("-------经纬度数据落库完毕-----------");
    }

}


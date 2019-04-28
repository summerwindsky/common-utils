package hyyd.rest;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.thunisoft.data.ws.entity.DataOnLine;
import com.thunisoft.data.ws.entity.StableData;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Title: RestfulUtil
 * Description: 
 * Company: 北京华宇元典信息服务有限公司
 *
 * @author tianxiupeng
 * @version 1.0
 * @date 2017年12月6日 下午2:15:26
 *
 */
@Service("restfulClient")
public class RestfulClient {

    private static Logger logger = LoggerFactory.getLogger(RestfulClient.class);

    @Resource(name = "restTemplate")
    private RestTemplate template;

    public RestfulClient() {

    }

    public String callRestful(String url, String request) throws Exception {
        ResponseEntity<String> response = template.postForEntity(url, request, String.class);
        return response.getBody();
    }

    public void callRestfulForPut(String url, String request) throws Exception {
        template.put(url, request, String.class);
    }

    public List<StableData> callStableDatasRestful(String url, String request,
            Class<StableData> class1) throws Exception {
        List<StableData> stableDatas = new ArrayList<StableData>();
        String data = callRestful(url, request);
        JSONArray jaonArray = JSONArray.fromObject(data);
        JSONObject jsonObject = new JSONObject();
        for (Object object : jaonArray) {
            jsonObject = (JSONObject) object;
            StableData stableData = (StableData) JSONObject.toBean(jsonObject, class1);
            stableDatas.add(stableData);
        }
        return stableDatas;
    }

    public List<DataOnLine> callDataOnLineRestful(String url, String request,
            Class<DataOnLine> class1) throws Exception {
        List<DataOnLine> stableDatas = new ArrayList<DataOnLine>();
        String data = callRestful(url, request);
        JSONArray jaonArray = JSONArray.fromObject(data);
        JSONObject jsonObject = new JSONObject();
        for (Object object : jaonArray) {
            jsonObject = (JSONObject) object;
            DataOnLine stableData = (DataOnLine) JSONObject.toBean(jsonObject, class1);
            stableDatas.add(stableData);
        }
        return stableDatas;
    }

}

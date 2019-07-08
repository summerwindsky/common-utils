package zj.mybatis.xsys;

import com.thunisoft.sync.aj.BaseMapper;
import com.thunisoft.sync.aj.Between;
import com.thunisoft.sync.aj.DataImportAdapter;
import com.thunisoft.sync.aj.xsys.mapper.XsysMapper;
import com.thunisoft.sync.model.Aj;
import com.thunisoft.sync.model.Yastml;
import com.thunisoft.sync.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 刑事一审service
 *
 * @author fanxf
 */
@Service("xsysService")
public class XsysService extends DataImportAdapter {


    @Autowired
    private XsysMapper xsysMapper;

    @Override
    public Constant.AjType getType() {

        return Constant.AjType.XSYS;
    }

    @Override
    public BaseMapper getMapper() {

        return xsysMapper;
    }

    @Override
    public void customTrans(Map<String, Aj> ajMap, Map<String, Yastml> yastmlMap, Between between) {
        return;
    }
}

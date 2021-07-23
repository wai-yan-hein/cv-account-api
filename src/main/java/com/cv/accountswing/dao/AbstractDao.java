package com.cv.accountswing.dao;

import com.cv.account.api.util.ZipFile;
import com.cv.accountswing.common.CVWork;
import com.cv.accountswing.util.Util1;
import com.google.gson.stream.JsonWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.util.Date;
import java.lang.reflect.ParameterizedType;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.data.JsonDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import org.hibernate.Criteria;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.jdbc.Work;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractDao<PK extends Serializable, T> {

    private static final Logger logger = LoggerFactory.getLogger(AbstractDao.class);
    private final Class<T> persistentClass;
    private ResultSet rs = null;
    private Statement statement = null;

    @SuppressWarnings("unchecked")
    public AbstractDao() {
        this.persistentClass = (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[1];
    }

    @Autowired
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    protected Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    @SuppressWarnings("unchecked")
    public T getByKey(PK key) {
        return (T) getSession().get(persistentClass, key);
    }

    public void persist(T entity) {
        try {
            getSession().saveOrUpdate(entity);
        } catch (Exception e) {
            logger.error("persiste  :" + e.getMessage());
        }
    }

    public void delete(T entity) {
        getSession().delete(entity);
    }

    protected Criteria createEntityCriteria() {
        return getSession().createCriteria(persistentClass);
    }

    public List<T> findHSQL(String hsql) {
        List<T> list = null;
        try {
            Query query = getSession().createQuery(hsql);
            list = query.list();
        } catch (Exception e) {
            logger.error("findHSQL  :" + e.getMessage());
        }
        return list;

    }

    public List findHSQLPC(String hsql, String filterName, String paramName, String paramValue) {
        Session session = getSession();
        Filter filter = session.enableFilter(filterName);
        filter.setParameter(paramName, paramValue);
        Query query = session.createQuery(hsql);
        List list = query.list();
        session.disableFilter(filterName);
        return list;
    }

    public List findHSQLList(String hsql) {
        Query query = getSession().createQuery(hsql);
        List list = query.list();
        return list;
    }

    public int execUpdateOrDelete(String hsql) {
        Query query = getSession().createQuery(hsql);
        int cnt = query.executeUpdate();
        return cnt;
    }

    public Object exeSQL(String hsql) {
        Query query = getSession().createQuery(hsql);
        Object obj = query.uniqueResult();
        return obj;

    }

    public Object findByKey(Class type, Serializable id) {
        Object obj = null;

        try {
            if (!id.equals("")) {

                obj = getSession().get(type, id);
                //tran.commit();
            }
        } catch (Exception ex) {
            logger.error("find1 : " + ex.getStackTrace()[0].getLineNumber() + " - " + ex.toString());
        }
        return obj;

    }

    public List<T> saveBatch(List<T> list) {
        list.forEach(obj -> {
            persist(obj);
        });
        return list;
    }

    public void execProc(String procName, String... parameters) {
        String strSQL = "{call " + procName + "(";
        String tmpStr = "";

        for (String parameter : parameters) {
            if (tmpStr.isEmpty()) {
                tmpStr = "?";
            } else {
                tmpStr = tmpStr + ",?";
            }
        }

        strSQL = strSQL + tmpStr + ")}";

        NativeQuery query = getSession().createSQLQuery(strSQL);
        int i = 0;
        for (String prm : parameters) {
            query.setParameter(i, prm);
            i++;
        }

        query.executeUpdate();
    }

    public void execSQL(String... strSql) throws Exception {
        for (String sql : strSql) {
            NativeQuery query = getSession().createSQLQuery(sql);
            query.executeUpdate();
        }
    }

    public String getGlLogSql(String glCode, String actionType, String userCode, Integer macId) {
        String strSql = "insert \n"
                + "into gl_log(gl_code, gl_date, created_date, modify_date, modify_by, description,\n"
                + "            source_ac_id, account_id, to_cur_id,from_cur_id, ex_rate, dr_amt,\n"
                + "            cr_amt, reference, dept_code, voucher_no, user_code, trader_code, \n"
                + "            cheque_no, comp_code, gst, tran_source, gl_vou_no, split_id, \n"
                + "            intg_upd_status, remark, from_desp, to_desp, naration,\n"
                + "            ref_no,mac_id,log_user_code,log_mac_id)\n"
                + "     select gl_code, gl_date, created_date, modify_date, modify_by, description,\n"
                + "            source_ac_id, account_id, to_cur_id,from_cur_id, ex_rate, dr_amt,\n"
                + "            cr_amt, reference, dept_code, voucher_no, user_code, trader_code, \n"
                + "            cheque_no, comp_code, gst, '" + actionType + "', gl_vou_no, split_id, \n"
                + "            intg_upd_status, remark, from_desp, to_desp, naration,\n"
                + "            ref_no, mac_id,'" + userCode + "'," + macId + "\n"
                + "	from gl where gl_code = '" + glCode + "'";
        return strSql;
    }

    public Object getAggregate(String sql) {
        NativeQuery query = getSession().createSQLQuery(sql);
        Object obj = query.uniqueResult();
        return obj;
    }

    public void doWork(Work work) {
        Session sess = getSession();
        sess.doWork(work);
    }

    public void doReportJasper(final String reportPath, final String filePath,
            final Map<String, Object> parameters, final String fontPath) throws Exception {
        Work work = (Connection con) -> {
            try {
                parameters.put("REPORT_CONNECTION", con);
                JasperPrint jp = getReport(reportPath, parameters, con, fontPath);
                JasperViewer jasperViewer = new JasperViewer(jp, false);
                jasperViewer.setTitle("Core Account Report");
                jasperViewer.setVisible(true);
            } catch (Exception ex) {
                logger.error("doReportPDF : " + ex.getMessage());
            }
        };
        doWork(work);
    }

    public void genJsonReport(final String path, final String reportPath, final String filePath,
            final Map<String, Object> parameters, final String fontPath) throws Exception {
        File file = null;
        file = new File(reportPath + ".jrxml");
        JsonDataSource ds = new JsonDataSource(new File(path), "data");
        JasperDesign jasperDesign = JRXmlLoader.load(file);
        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, ds);
        if (jasperPrint != null) {
            JasperViewer.viewReport(jasperPrint, false);

        }
    }

    private JasperPrint getReport(String reportPath, Map<String, Object> parameters,
            Connection con, String fontPath) throws Exception {
        JasperPrint jp;

        reportPath = reportPath + ".jasper";
        JasperReportsContext jasperReportsContext = DefaultJasperReportsContext.getInstance();
        jasperReportsContext.setProperty("net.sf.jasperreports.default.pdf.font.name", fontPath);
        jasperReportsContext.setProperty("net.sf.jasperreports.default.pdf.encoding", "Identity-H");
        jasperReportsContext.setProperty("net.sf.jasperreports.default.pdf.embedded", "true");

        jp = JasperFillManager.fillReport(reportPath, parameters, con);

        return jp;
    }

    public String genJSONFile(final String strSql) throws Exception {

        String filePath = Util1.getAppWorkFolder() + File.separator + "zipFile" + File.separator;
        String fileName = UUID.randomUUID().toString();
        String zipFileName = "";

        CVWork work = new CVWork(strSql);
        doWork(work);
        Connection con = work.getCon();

//        while (!work.isStatus()) {
//            System.out.println("Waiting.");
//        }
        try {
            PreparedStatement pstmt = con.prepareStatement(strSql);
            ResultSet rsFile = pstmt.executeQuery();
            int ttlCols = rsFile.getMetaData().getColumnCount();
            FileOutputStream fos = new FileOutputStream(filePath + fileName + ".json");
            OutputStreamWriter isr = new OutputStreamWriter(fos, "UTF-8");

            try ( JsonWriter writer = new JsonWriter(isr)) {
                writer.beginObject();
                writer.name("data");
                writer.beginArray();

                while (rsFile.next()) {

                    writer.beginObject();
                    for (int i = 1; i <= ttlCols; i++) {

                        String colName = rsFile.getMetaData().getColumnName(i);
                        writer.name(colName).value(rsFile.getString(colName));
                    }
                    writer.endObject();
                }

                writer.endArray();
                writer.endObject();
                writer.close();
            }
            ZipFile zf = new ZipFile();
            zipFileName = zf.zipFiles(filePath, fileName + ".json");

        } catch (IOException | SQLException ex) {
            logger.error("getResultSet : " + strSql + " : " + ex.getMessage());
        }

        if (zipFileName.isEmpty()) {
            logger.error("Invalid File : " + zipFileName);
        }
        return zipFileName;
    }

    public ResultSet getResultSet(final String strSql) throws Exception {
        rs = null;

        Work work = new Work() {
            @Override
            public void execute(Connection con) {
                try {
                    PreparedStatement pstmt = con.prepareStatement(strSql);
                    rs = pstmt.executeQuery();
                } catch (SQLException ex) {
                    logger.error("getResultSet : " + strSql + " : " + ex.getMessage());
                }
            }
        };
        doWork(work);

        return rs;
    }

    public ResultSet getPro(String procName, String... parameters) {

        String strSQL = "{call " + procName + "(";
        ResultSet resultSet = null;
        CVWork work = new CVWork(strSQL);
        doWork(work);
        Connection con = work.getCon();

        try {
            String tmpStr = "";
            for (int i = 0; i < parameters.length; i++) {
                if (tmpStr.isEmpty()) {
                    tmpStr = "?";
                } else {
                    tmpStr = tmpStr + ",?";
                }
            }

            strSQL = strSQL + tmpStr + ")}";

            statement = con.prepareCall(strSQL);

            int i = 1;
            for (Object obj : parameters) {
                if (obj instanceof Date) {
                    ((CallableStatement) statement).setString(i, Util1.toDateTimeStrMYSQL(obj.toString()));
                } else {
                    ((CallableStatement) statement).setString(i, obj.toString());
                }

                i++;
            }
            resultSet = ((CallableStatement) statement).executeQuery();

        } catch (SQLException ex) {
            logger.error("getPro2 : " + ex.getStackTrace()[0].getLineNumber() + " - " + ex.getMessage());
        }

        return resultSet;
    }

    public String genJSON(final String strSql) throws Exception {

        String filePath = Util1.getAppWorkFolder() + File.separator + "zipFile" + File.separator;
        String fileName = UUID.randomUUID().toString();
        String jsonFile = "";

        CVWork work = new CVWork(strSql);
        doWork(work);
        Connection con = work.getCon();

//        while (!work.isStatus()) {
//            System.out.println("Waiting.");
//        }
        try {
            PreparedStatement pstmt = con.prepareStatement(strSql);
            ResultSet rsFile = pstmt.executeQuery();
            int ttlCols = rsFile.getMetaData().getColumnCount();
            FileOutputStream fos = new FileOutputStream(filePath + fileName + ".json");
            OutputStreamWriter isr = new OutputStreamWriter(fos, "UTF-8");

            try ( JsonWriter writer = new JsonWriter(isr)) {
                writer.beginObject();
                writer.name("data");
                writer.beginArray();

                while (rsFile.next()) {

                    writer.beginObject();
                    for (int i = 1; i <= ttlCols; i++) {

                        String colName = rsFile.getMetaData().getColumnName(i);
                        writer.name(colName).value(rsFile.getString(colName));
                    }
                    writer.endObject();
                }

                writer.endArray();
                writer.endObject();
                writer.close();
            }

            jsonFile = filePath + fileName + ".json";

        } catch (IOException | SQLException ex) {
            logger.error("getResultSet : " + strSql + " : " + ex.getMessage());
        }

        return jsonFile;
    }
}

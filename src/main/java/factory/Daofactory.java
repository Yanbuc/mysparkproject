package factory;

import dao.*;
import daoimpl.*;

public class Daofactory {
    public  static ITask getTaskImpl(){
        return new TaskDaoImpl();
    }
    public static ISessionAggrStat getSessionAggrStat(){
        return new SessionAggrStatImpl();
    }
    public static ISessionRandomExtract getSessionRandomExtract(){
        return new SessionRandomExtractImpl();
    }
    public static ITop10Category getItop10Category(){
        return new Top10CategoryImpl();
    }
    public static ITop10CategorySession getITop10CategorySession(){
        return new Top10CategorySessionImpl();
    }
    public static IPageSplitConvertRate getIPageSplitConvertRate(){
        return new PageSplitConvertRateImpl();
    }
    public static IAreaTop3Product getIAreaTop3Product(){
        return new AreaTop3ProductImpl();
    }
}

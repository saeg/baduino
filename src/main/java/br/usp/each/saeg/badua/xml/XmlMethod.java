package br.usp.each.saeg.badua.xml;

import java.math.*;
import java.util.*;

import javax.xml.bind.annotation.*;

/**
 * @author Danilo Mutti (dmutti@gmail.com)
 */
public class XmlMethod {

    private String name;
    private BigDecimal score = new BigDecimal(XmlInput.nextScore());
    private int loc;
    private ArrayList<XmlStatement> statements = new ArrayList<XmlStatement>();
    private int number;
    private int start;
    private int end;
    private int close;
    private String content;
    private Integer scriptPosition;
    private Float scriptScore;

//    @XmlAttribute(name="suspicious-value")
//    public BigDecimal getScore() {
//        return score;
//    }
//    public void setScore(BigDecimal score) {
//        if (null != score) {
//            this.score = score;
//        }
//    }

    @XmlElement(name="requirement")
    public ArrayList<XmlStatement> getStatements() {
        return statements;
    }
    
    public void setStatements(ArrayList<XmlStatement> statements) {
//    	Collections.sort(statements, new Comparator<XmlStatement>() {
//            @Override
//            public int compare(XmlStatement o1, XmlStatement o2) {
//          	  int resp = Integer.compare(o1.getDef(), o2.getDef());
//          	  if(resp != 0) return resp;
//          	  resp = Integer.compare(o1.getUse(), o2.getUse());
//          	  if(resp != 0) return resp;
//          	  resp = Integer.compare(o1.getTarget(), o2.getTarget());
//          	  if(resp != 0) return resp;
//          	  return o1.getVar().compareTo(o2.getVar());
//
//            }
//        });
        this.statements = statements;
    }
    public void addStatements(XmlStatement statement) {
        if (statement != null) {
            statements.add(statement);
        }
    }

//    @XmlAttribute(name="location")
//    public int getLoc() {
//        return loc;
//    }
//    public void setLoc(int loc) {
//        this.loc = loc;
//    }

//    public XmlStatement byRelativeLoc(Integer arg) {
//        List<XmlStatement> result = new ArrayList<XmlStatement>();
//        for (int i = 0, j = statements.size(); i < j; i++) {
//            XmlStatement stmt = statements.get(i);
//            if (arg.equals(stmt.getLoc() - loc)) {
//                result.add(stmt);
//            }
//        }
//        return max(result);
//    }

//    public XmlStatement byAbsoluteLoc(Integer arg) {
//        List<XmlStatement> result = new ArrayList<XmlStatement>();
//        for (int i = 0, j = statements.size(); i < j; i++) {
//            XmlStatement stmt = statements.get(i);
//            if (arg.equals(stmt.getLoc())) {
//                result.add(stmt);
//            }
//        }
//        return max(result);
//    }

//    private XmlStatement max(List<XmlStatement> result) {
//        if (result.isEmpty()) {
//            return null;
//        }
//        Collections.sort(result, new Comparator<XmlStatement>() {
//            @Override
//            public int compare(XmlStatement o1, XmlStatement o2) {
//                return o2.getScore().compareTo(o1.getScore());
//            }
//        });
//        return result.get(0);
//    }

    @XmlAttribute(name="name")
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

//    @XmlAttribute(name="number")
//    public int getNumber() {
//        return number;
//    }
//    public void setNumber(int number) {
//        this.number = number;
//    }

//    @XmlTransient
//    public int getStart() {
//        return start;
//    }
//    public void setStart(int start) {
//        this.start = start;
//        this.setLoc(start);
//    }

//    @XmlTransient
//    public int getEnd() {
//        return end;
//    }
//    public void setEnd(int end) {
//        this.end = end;
//    }
//
//    @XmlTransient
//    public int getClose() {
//        return close;
//    }
//    public void setClose(int close) {
//        this.close = close;
//    }
//
//    @XmlTransient
//    public String getContent() {
//        return content;
//    }
//    public void setContent(String content) {
//        this.content = content;
//    }

//    @XmlAttribute(name="position")
//    public Integer getScriptPosition() {
//        return scriptPosition;
//    }
//    public void setScriptPosition(Integer scriptPosition) {
//        if (scriptPosition != null) {
//            this.scriptPosition = scriptPosition;
//        } else {
//            this.scriptPosition = 0;
//        }
//    }
//
//    @XmlAttribute(name="methodsusp")
//    public Float getScriptScore() {
//        return scriptScore;
//    }
//    public void setScriptScore(Float scriptScore) {
//        if (scriptScore != null) {
//            this.scriptScore = scriptScore;
//        } else {
//            this.scriptScore = 0F;
//        }
//
//    }
}

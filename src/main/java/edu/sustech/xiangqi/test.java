package edu.sustech.xiangqi;

import java.sql.SQLException;

import edu.sustech.xiangqi.model.DBOperationBoard;
import edu.sustech.xiangqi.model.DBOperationUser;

public class test {
    public static void main(String[] args) {
        try{
            DBOperationUser.createTable();
            DBOperationBoard.createTable();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
}

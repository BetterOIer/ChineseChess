# ChineseChess
Chinese Chess Group Project of Sustech CS109 2025 Fall.

## Some Ideas

### 项目架构

1. 棋盘端
   1. 旗子移动规则——Basic
   2. 终点判定——Basic
   3. 移动提示——Basic
2. 用户端
   1. 下棋模式
      1. 单单棋盘——Basic
      2. 无线双人游戏(use TCP/IP) ——Advance
      3. 单人(with AI) ——Advance
   2. 用户交互
      1. 登录登出——Basic
      2. 启动关闭——Basic
      3. 事件反馈（比如格子不能走、将军提示，下一步提示）——Advance
         1. 声音——Advance
         2. 提示栏——Advance——Advance
   3. 用户信息储存（针对2.1.2, 2.1.3）：数据库——Basic
      1. 密码方面：哈希值
   4. 棋盘信息储存：需要棋盘段反馈：数据库——Basic


## Schedule

### 11.10 - 11.23

1. ui代码已重构，确保每一个文件格式如下
   ```java
   public class frameName extends JFrame{
      
      //some subcomponent
      //basically just thoses very complex component like JPanel 
      private final panelNamePanel panelName;

      public frameName(what-you-need){
         setTitle("...");
         //......
      }

   }

   //if needed:
   class panelNamePanel extends JPanel{
      public panelNamePanel(){
         //......
      }
   }
   ```
   这样，在一个窗口切换到另一个窗口时，代码可以几行解决不会显得冗长。
   ```java
   setVisible(false); //Hide this window
   ChessBoardModel model = this.archives.get(Idx);//
   ChessBoard chessBoard = new ChessBoard(model);//init next window
   chessBoard.setVisible(true); //show next window
   ```

2. 数据库已接入
   请注意更新你的IDE的项目配置（应该会自动更新的来着。。。）  
   注意：请在`xiangqi`文件夹下新建`database`文件夹，否则会跑不起来！！！
   



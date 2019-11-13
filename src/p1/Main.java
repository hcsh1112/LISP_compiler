package p1;


//package PL105_10227105 ;
import java.io.File;

import java.io.IOException;

import java.util.*;

class Main {

  public static int sChar = 0 ; // check input the char where the column in a line 
  public static int sColumn = 0 ;
  public static int sPeekChar = 0 ; // 
  public static int sLine = 0 ; // now input in which line
  public static String sLineInput = "" ; // a string contain a line input
  public static String sSpace = "" ; // prettyPrint need it
  public static boolean sPrint = false ; // above
  public static int sTime = 0 ; // check atom or exp
  public static int sLevel = 0 ;
  public static boolean sLP = false ;
  
  public static int sErrorLevel = 0 ;
  
  public static int sLevelTree = 0 ;
  
  public static boolean sWhiteSpace = false ;
  public static String sErrorMsg = "" ;
  public static boolean sExist = false ;
  public static boolean sLeftTree = false ;
  public static boolean sQuote = false ;
  public static boolean sFunction = false ;
  
  public static ArrayList<Node> sEnvironTitle = new ArrayList <Node>() ;
  public static ArrayList<Node> sEnviron = new ArrayList <Node>() ;
  public static ArrayList<Node> sEnvironFun = new ArrayList <Node>() ;
  
  public static boolean sDefine = false ;
  
  public static int slambdaNum = 0 ;
  

  public static Scanner sca = new Scanner( System.in ) ; // read object
  // public static Scanner sca ;
  public static void main( String[] args ) throws Throwable {
    // TODO Auto-generated method stub
    // File f = new File( "C:/Users/user/Desktop/2_1q.txt" ) ;
    // File f = new File( "test.txt" ) ;
    // sca = new Scanner( f ) ;
    int uTestNum = sca.nextInt() ;
    
    System.out.println( "Welcome to OurScheme!" );
    System.out.println( "" );
    
    while ( true ) {
      Node root ;
      try {   
        System.out.print( "> " );
        root = ReadExpShell() ;
        sLevelTree = 0 ;
        slambdaNum = 0 ;
        sErrorLevel++ ;
        root = EvalSExp( root ) ;
        if ( root.GetData() != " " ) sPrint = false ;
        else sPrint = true ;
        
        if ( root.GetLeftNode() == null && root.GetRightNode() == null && root.GetData() != " " ) // atom
          PrettyPrint( root ) ;
  
        else if ( root.GetLeftNode().GetData().equals( "#<procedure exit>" ) ) {
          if ( !root.GetRightNode().GetData().equals( "nil" ) ) 
            PrettyPrint( root ) ;
          else {  
            System.out.println( "" );
            System.out.println( "Thanks for using OurScheme!" ) ;
            return ;
          } // else 
        } // else if
          
        else 
          PrettyPrint( root ) ;
 
        
        
        System.out.println( "" ) ;
          
        sExist = false ;
        // System.out.println( sColumn );
    
        

        sColumn = 0 ;
        
        // sChar = 0 ;
        
      } catch ( Exception e ) {
        // TODO Auto-generated catch block
        sDefine = false ;
        // System.out.println( e.getMessage() );
      } // catch
      
      
      if ( !sErrorMsg.equals( "" ) ) {
        System.out.println( sErrorMsg );
        
        if ( sErrorMsg.equals( "ERROR (no more input) : END-OF-FILE encountered" ) ) { 
          System.out.println( "Thanks for using OurScheme!" ) ;
          return ;
        } // if
        
        

        
        System.out.println( "" );
        if ( sErrorLevel == 0 ) sChar = sLineInput.length() ;
        else sColumn = 0 ;
        
        sErrorMsg = "" ;
       
        
      } // if
      
      
      if ( PeekCharNoSpace( sChar ) == '\0' || PeekCharNoSpace( sChar ) == ';' ) sLine = 0 ;
      else sLine = 1 ;
      
      sExist = false ;
      sSpace = "" ;
      sErrorLevel = 0 ;
    } // while 
    
  } // main() 
  
  /*
  public static void preorder( Node root) {
    if( root !=  null ) {
      root.Printf();
      preorder(root.GetLeftNode());
      preorder(root.GetRightNode());
    }
  } // preoeder
  
  */
  
  public static Node ReadExp() throws Exception {
    Token tempToken = GetToken() ;
    
    Node head = new Node() ;
    Node root = new Node() ;
    
    head = root ;
    
    boolean dot = false ;
    boolean firstAtom = false ;
    boolean full = false ;
    
    while ( tempToken != null ) {
      
      if ( tempToken.GetType().equals( "LEFT-PAREN" ) ) {
        
        if ( full ) {
          sErrorMsg = "ERROR (unexpected token) : ')' expected when token at Line " + sLine
                      + " Column " + sColumn ;
          sErrorMsg = sErrorMsg + " is >>" + tempToken.Get() + "<<" ;
          throw null ;
        } // if
        
        
        Node temp = ReadExp() ;
        
        if ( temp != null ) {
          if ( !firstAtom ) {
            root.SetLeft( temp ) ;
            firstAtom = true ;
          } // if
          
          else if ( !dot ) {
            Node temp2  = new Node() ;
            temp2.SetLeft( temp ) ;
            root.SetRight( temp2 ) ;
            root = root.GetRightNode() ;
          } // else if
          
          else {
            root.SetRight( temp ) ;
            full = true ;
          } // else 
        } // if
      } // if
      
      else if ( tempToken.Get().equals( "quote" ) && tempToken.GetQuote() ) {

        Node temp = ReadExpShell() ; // 'Exp
        Node qnode = new Node() ;
        Node expnode = new Node() ;
        Node rootright = new Node() ;
        
        
        qnode.SetLeft( new Node( tempToken ) );
        
        expnode.SetLeft( temp ) ;
        expnode.SetRight( new Node( new Token( "nil" ) ) ) ;
        
        
        qnode.SetRight( expnode ) ;
        /*
        rootright.SetLeft( qnode ) ;
        root.SetRight( rootright ) ;
        root = root.GetRightNode() ;
        */
        if ( !firstAtom ) {
          root.SetLeft( qnode ) ;
          firstAtom = true ;
        } // if
        
        else {
          rootright.SetLeft( qnode ) ;
          root.SetRight( rootright ) ;
          root = root.GetRightNode() ;
        } // else 
        
        
        
        // firstAtom = true ;
      } // else if
      
      else if ( tempToken.GetAtom() ) { // atom
        if ( !firstAtom ) {
          root.SetLeft( new Node( tempToken ) ) ;
          firstAtom = true ;
        } // if
        
        else if ( !dot ) {
          Node temp  = new Node() ;
          Node tempHead = new Node() ;
          temp.SetLeft( new Node( tempToken ) ) ;
          root.SetRight( temp ) ; 
          root = root.GetRightNode() ; 
        } // else if
        
        else if ( full ) {
          sErrorMsg = "ERROR (unexpected token) : ')' expected when token at Line " + sLine
                      + " Column " + sColumn ;
          sErrorMsg = sErrorMsg + " is >>" + tempToken.Get() + "<<" ;
          throw null ;
        } // else if
        /*
        else if ( full ) { // error ( 1 . 2  2 )
          
        } // else if
        */
        else { // . X
          root.SetRight( new Node( tempToken ) ) ;
          full = true ;
        } // else 
      } // else if
      
      else if ( tempToken.GetType().equals( "DOT" ) && !firstAtom ) {
        sErrorMsg = "ERROR (unexpected token) : atom or '(' expected when token at Line " + sLine
                    + " Column " + sColumn ;
        sErrorMsg = sErrorMsg + " is >>" + tempToken.Get() + "<<" ;
        throw null ;
      } // else if
      
      else if ( tempToken.GetType().equals( "DOT" ) && dot ) {
        sErrorMsg = "ERROR (unexpected token) : ')' expected when token at Line " + sLine
                    + " Column " + sColumn ;
        sErrorMsg = sErrorMsg + " is >>" + tempToken.Get() + "<<" ;
        throw null ;
      } // else if
      
      else if ( tempToken.GetType().equals( "DOT" ) ) dot = true ;
      
      else if ( tempToken.GetType().equals( "RIGHT-PAREN" ) ) {
        if ( !firstAtom ) 
          return new Node( new Token( "nil" ) ) ;
        
        else if ( !dot ) 
          root.SetRight( new Node( new Token( "nil" ) ) ) ; 
        
        else if ( !full ) { // error
          sErrorMsg = "ERROR (unexpected token) : atom or '(' expected when token at Line " + sLine
                      + " Column " + sColumn ;
          sErrorMsg = sErrorMsg + " is >>" + tempToken.Get() + "<<" ;
          throw null ;
        } // else if
        
        return head  ;
      } // else if
      
      
      tempToken = GetToken() ;
    } // while
    
    return null ; // error
    
    
  } // ReadExp() 
  
  public static Node ReadExpShell() throws Exception {
    Token tempToken = GetToken() ;
    sPrint = false ;
    Node root = null ;
    if ( tempToken.GetType().equals( "LEFT-PAREN" ) ) {
      root = ReadExp() ;
      sPrint = true ;
      return root ;
    } // if 
    
    else if ( tempToken.Get().equals( "quote" ) && tempToken.GetQuote() ) {
      root = new Node() ;
      root.SetLeft( new Node( tempToken ) ) ;
      Node expnode = new Node() ;
      Node temp = ReadExpShell() ;
      expnode.SetLeft( temp ) ;
      expnode.SetRight( new Node( new Token( "nil" ) ) ) ;
      root.SetRight( expnode ) ;
      sPrint = true ;
      return root ;
    } // else if
    
    else { // not exp
      
      
      
      if ( tempToken.Get().equals( "." ) ) {
        sErrorMsg = "ERROR (unexpected token) : atom or '(' expected when token at Line " + sLine
                    + " Column " + sColumn ;
        sErrorMsg = sErrorMsg + " is >>" + tempToken.Get() + "<<" ;
        throw null ;
      } // if
      
      if ( tempToken.Get().equals( ")" ) ) {
        sErrorMsg = "ERROR (unexpected token) : atom or '(' expected when token at Line " + sLine
                    + " Column " + sColumn ;
        sErrorMsg = sErrorMsg + " is >>" + tempToken.Get() + "<<" ;
        throw null ;
      } // if
    
      root = new Node( tempToken ) ;
      return root ;
    } // else 
  } // ReadExpShell()
  
  /*
  public static int FindEnviron( Node temp ) {
    for ( int i = 0 ; i < sEnvironTitle.size() ; i++ ) {
      if ( temp.GetData().equals( sEnvironTitle.get( i ).GetData() ) ) return i ;
    } // for
    
    return -1 ;
  } // FindEnviron()
  */
  
  /*
  public static int FindEnviron( Node temp ) {
    return sEnvironTitle.lastIndexOf( temp.GetData() ) ;
  } // FindEnviron()
  */
  
  public static int FindEnviron( Node temp ) {
    for ( int i = sEnvironTitle.size() - 1 ; i >= 0 ; i-- ) {
      if ( temp.GetData().equals( sEnvironTitle.get( i ).GetData() ) ) return i ;
    } // for
    
    return -1 ;
  } // FindEnviron()
  
  public static int FindFuntion( Node temp ) {
    for ( int i = sEnvironFun.size() - 1 ; i >= 0 ; i-- ) {
      if ( temp.GetData().equals( sEnvironFun.get( i ).GetData() ) ) return i ;
    } // for
    
    return -1 ;
  } // FindFuntion()
  
  public static Node ProcedureFun( Node oExp, Node num ) {
    Node copy = CopyNode( oExp ) ;
    Node copynum = CopyNode( num ) ;
    Node var = null ;
    if ( FindFuntion( oExp ) != -1 ) {
      oExp = EvalSExp( oExp ) ;
      copy = CopyNode( oExp ) ;
    } // if
    
    
    var = copy.GetLeftNode().GetRightNode() ;
      
      
    ArrayList<Node> temptitle = new ArrayList <Node>() ;
    ArrayList<Node> tempenviron = new ArrayList <Node>() ;
    
    int level = 0 ;
    Node target = null ;
    Node title = null ;
    Node exp = null ;
    while ( ! var.GetData().equals( "nil" ) ) {
      title = var.GetLeftNode() ;
      if ( FindFuntion( copynum.GetLeftNode() ) != -1 ) 
        sFunction = true ;
      exp = EvalSExp( copynum.GetLeftNode() ) ;
      sFunction = false ;
      temptitle.add( title ) ;
      tempenviron.add( exp ) ;
      
      var = var.GetRightNode() ;
      copynum = copynum.GetRightNode() ;
      level++ ;
    } // while
    
    for ( int i = 0 ; i < level ; i++ ) {
      sEnvironTitle.add( temptitle.get( i ) ) ;
      sEnviron.add( tempenviron.get( i ) ) ;
    } // for
    
    copy = copy.GetRightNode() ;
    while ( ! copy.GetData().equals( "nil" ) ) {
      target = EvalSExp( copy.GetLeftNode() ) ;
      copy = copy.GetRightNode() ;
    } // while 
    
    while ( level != 0 ) {
      sEnvironTitle.remove( sEnvironTitle.size() - 1 );
      sEnviron.remove( sEnviron.size() - 1 );
      level-- ;
    } // while
    
    return target ;
  } // ProcedureFun()
  

  
  public static Node EvalSExp( Node oExp ) {
    sLevelTree++ ;
    if ( oExp != null  ) {
      if ( oExp.GetLeftNode() == null ) {
        if ( oExp.GetTokenType().equals( "cmd" ) )
          return new Node( new Token( "#<procedure " + oExp.GetData() + ">" ) ) ;
        else if ( !oExp.GetTokenType().equals( "SYMBOL" ) ) return oExp ; // Node is integer or float 
        else if ( oExp.GetTokenType().equals( "SYMBOL" ) ) {
          if ( FindEnviron( oExp ) != -1 ) {
            Node temp = sEnviron.get( FindEnviron( oExp ) ) ;
            if ( FindFuntion( temp ) != -1 ) {
              if ( sLevelTree == 1 ) 
                return new Node( new Token( "#<procedure " + temp.GetData() + ">" ) ) ; 
              else 
                return EvalSExp( temp ) ;   
            } // if
            
            else if ( temp.GetData() == " " ) {
              sPrint = true ;
              if ( temp.GetLeftNode().GetData().equals( "#<procedure lambda>" ) )
                return temp.GetLeftNode() ;
              
              else if ( FindFuntion( oExp ) != -1 &&  sLevelTree == 1 ) // is function
                return new Node( new Token( "#<procedure " + oExp.GetData() + ">" ) ) ;
              
            } // else if
               
            
            return temp ;
          } // if
                   
          else {
            sErrorMsg = "ERROR (unbound symbol) : " + oExp.GetData() ; 
            throw null ;
          } // else 
          
        } // else if
      } // if
      
      if ( FindEnviron( oExp.GetLeftNode() ) != -1 ) { // command define
        Node temp = sEnviron.get( FindEnviron( oExp.GetLeftNode() ) ) ;
        if ( temp.GetLeftNode() != null && temp.GetLeftNode().GetData() != " "  )
          oExp.SetLeft( temp ) ;
        else if ( CheckCommand( temp ) ) 
          oExp.SetLeft( temp ) ;
        else {
          int count = sLevelTree ;
          Node fun = ProcedureFun( temp, oExp.GetRightNode() ) ;
          
          if ( fun.GetData() == " " ) {
            if ( fun.GetLeftNode().GetData().equals( "#<procedure lambda>" ) && count == 1 )
              return fun.GetLeftNode() ;
          } // if
          
          return fun ;
        } // else
        
      } // if
      
      if ( oExp.GetLeftNode().GetData() == " " ) oExp.SetLeft( EvalSExp( oExp.GetLeftNode() ) ) ;
      
      if ( oExp.GetRightNode() != null ) {
        if ( oExp.GetRightNode().GetData() != " " && ! oExp.GetRightNode().GetData().equals( "nil" ) ) {
          sPrint = true ;
          System.out.print( "ERROR (non-list) : " ) ;
          PrettyPrint( oExp ) ;
          System.out.println( "" ) ;
          throw null ;
        } // if
          
      } // if
      
      if ( oExp.GetLeftNode().GetData().equals( "lambda" ) ) {
        slambdaNum = sLevelTree ;
        oExp = CmdLambda( oExp ) ;
      } // if
        
        
      else if ( oExp.GetLeftNode().GetTokenType().equals( "cmd" ) )
        oExp.SetLeft( new Node( new Token( "#<procedure " + oExp.GetLeftNode().GetData() + ">" ) ) ) ;
      
      if ( oExp.GetLeftNode().GetData().equals( "#<procedure exit>" ) ) {
        if ( oExp.GetRightNode().GetData().equals( "nil" ) && sLevelTree == 1 ) return oExp ;
        else if ( sLevelTree != 1 ) {
          sErrorMsg = "ERROR (level of EXIT)" ;           
          throw null ;
        } // else if
        else {
          sErrorMsg = "ERROR (incorrect number of arguments) : exit" ;           
          throw null ;
        } // else 
      } // if
        
      if ( oExp.GetLeftNode().GetData().equals( "#<procedure cons>" ) ) return CmdCons( oExp ) ;
      if ( oExp.GetLeftNode().GetData().equals( "#<procedure list>" ) ) return CmdList( oExp ) ;
      if ( oExp.GetLeftNode().GetData().equals( "#<procedure quote>" ) ) return CmdQuote( oExp ) ;
      if ( oExp.GetLeftNode().GetData().equals( "#<procedure define>" ) && sLevelTree == 1 ) {
        CmdDefine( oExp ) ;
        return null ;
      } // if
        
      if ( oExp.GetLeftNode().GetData().equals( "#<procedure car>" ) ) return CmdCar( oExp ) ;
      if ( oExp.GetLeftNode().GetData().equals( "#<procedure cdr>" ) ) return CmdCdr( oExp ) ;
      if ( oExp.GetLeftNode().GetData().equals( "#<procedure clean-environment>" ) && sLevelTree == 1  ) {
        CmdClean() ;
        return null ;
      } // if
        
      if ( oExp.GetLeftNode().GetData().equals( "#<procedure atom?>" ) ) return CmdAtom( oExp ) ;
      if ( oExp.GetLeftNode().GetData().equals( "#<procedure pair?>" ) ) return CmdPair( oExp ) ;
      if ( oExp.GetLeftNode().GetData().equals( "#<procedure list?>" ) ) return CmdListQ( oExp ) ;
      if ( oExp.GetLeftNode().GetData().equals( "#<procedure null?>" ) ) return CmdNull( oExp ) ;
      if ( oExp.GetLeftNode().GetData().equals( "#<procedure integer?>" ) ) return CmdInt( oExp ) ;
      if ( oExp.GetLeftNode().GetData().equals( "#<procedure real?>" ) ) return CmdReal( oExp ) ;
      if ( oExp.GetLeftNode().GetData().equals( "#<procedure number?>" ) ) return CmdNumber( oExp ) ;
      if ( oExp.GetLeftNode().GetData().equals( "#<procedure string?>" ) ) return CmdString( oExp ) ;
      if ( oExp.GetLeftNode().GetData().equals( "#<procedure boolean?>" ) ) return CmdBoolean( oExp ) ;
      if ( oExp.GetLeftNode().GetData().equals( "#<procedure symbol?>" ) ) return CmdSymbol( oExp ) ;
      if ( oExp.GetLeftNode().GetData().equals( "#<procedure +>" ) ) return CmdPlus( oExp ) ;
      if ( oExp.GetLeftNode().GetData().equals( "#<procedure ->" ) ) return CmdSub( oExp ) ;
      if ( oExp.GetLeftNode().GetData().equals( "#<procedure *>" ) ) return CmdMult( oExp ) ;
      if ( oExp.GetLeftNode().GetData().equals( "#<procedure />" ) ) return CmdDiv( oExp ) ;
      if ( oExp.GetLeftNode().GetData().equals( "#<procedure not>" ) ) return CmdNot( oExp ) ;
      if ( oExp.GetLeftNode().GetData().equals( "#<procedure and>" ) ) return CmdAnd( oExp ) ;
      if ( oExp.GetLeftNode().GetData().equals( "#<procedure or>" ) ) return CmdOr( oExp ) ;
      if ( oExp.GetLeftNode().GetData().equals( "#<procedure >>" ) ) return CmdBig( oExp ) ;
      if ( oExp.GetLeftNode().GetData().equals( "#<procedure >=>" ) ) return CmdBigEq( oExp ) ;
      if ( oExp.GetLeftNode().GetData().equals( "#<procedure <>" ) ) return CmdSmall( oExp ) ;
      if ( oExp.GetLeftNode().GetData().equals( "#<procedure <=>" ) ) return CmdSmallEq( oExp ) ;
      if ( oExp.GetLeftNode().GetData().equals( "#<procedure =>" ) ) return CmdEq( oExp ) ;
      if ( oExp.GetLeftNode().GetData().equals( "#<procedure string-append>" ) ) 
        return CmdStringAppend( oExp ) ;
      if ( oExp.GetLeftNode().GetData().equals( "#<procedure string>?>" ) ) return CmdStringBig( oExp ) ;
      if ( oExp.GetLeftNode().GetData().equals( "#<procedure string<?>" ) ) return CmdStringSmall( oExp ) ;
      if ( oExp.GetLeftNode().GetData().equals( "#<procedure string=?>" ) ) return CmdStringEq( oExp ) ;
      if ( oExp.GetLeftNode().GetData().equals( "#<procedure eqv?>" ) ) return CmdEqv( oExp ) ;
      if ( oExp.GetLeftNode().GetData().equals( "#<procedure equal?>" ) ) return CmdEqual( oExp ) ;
      if ( oExp.GetLeftNode().GetData().equals( "#<procedure begin>" ) ) return CmdBegin( oExp ) ;
      if ( oExp.GetLeftNode().GetData().equals( "#<procedure if>" ) ) return CmdIf( oExp ) ;
      if ( oExp.GetLeftNode().GetData().equals( "#<procedure cond>" ) ) return CmdCond( oExp ) ;
      if ( oExp.GetLeftNode().GetData().equals( "#<procedure let>" ) ) return CmdLet( oExp ) ;
      
      if ( oExp.GetLeftNode().GetData().equals( "#<procedure lambda>" ) ) {
        if ( sLevelTree == 1 ) 
          return oExp.GetLeftNode() ;
     
        else return oExp ;
      } // if
      
  
      
      if ( oExp.GetLeftNode().GetLeftNode() != null ) {
        if ( oExp.GetLeftNode().GetLeftNode().GetData().equals( "#<procedure lambda>" ) ) 
          return ExeLambda( oExp ) ;
        
      } // if
      
      
      if ( oExp.GetLeftNode().GetData().equals( "#<procedure define>" ) && sLevelTree != 1 ) {
        sErrorMsg = "ERROR (level of DEFINE)" ;    
        throw null ;
      } // if
        
      if ( oExp.GetLeftNode().GetData().equals( "#<procedure clean-environment>" ) && sLevelTree != 1 ) {
        sErrorMsg = "ERROR (level of CLEAN-ENVIRONMENT)" ;     
        throw null ;
      } // if
        

      
      
    
      else if ( oExp.GetLeftNode().GetTokenType().equals( "INT" ) ) {
        sErrorMsg = "ERROR (attempt to apply non-function) : " + oExp.GetLeftNode().GetData() ;           
        throw null ;
      } // else if
      
      else { // left is not command
        if ( FindEnviron( oExp.GetLeftNode() ) != -1 ) {
          sErrorMsg = "ERROR (attempt to apply non-function) : " + oExp.GetLeftNode().GetData() ;           
          throw null ; 
        } // if
        
        else if ( oExp.GetLeftNode().GetData() == " " ) { // is a tree
          System.out.print( "ERROR (attempt to apply non-function) : " ) ;
          PrettyPrint( oExp.GetLeftNode() ) ;
          System.out.println( "" ) ;
          throw null ;
        } // else if
        
        else if ( oExp.GetLeftNode().GetTokenType().equals( "SYMBOL" ) ) {
          sErrorMsg = "ERROR (unbound symbol) : " + oExp.GetLeftNode().GetData() ; 
          sPrint = true ;
          throw null ;
        } // else if
        
        else {
          sErrorMsg = "ERROR (attempt to apply non-function) : " + oExp.GetLeftNode().GetData() ;           
          throw null ;
        } // else 
        
      } // else 
    } // if
    
    return null ;
  } // EvalSExp()
  
  
  public static Node CmdCons( Node oExp ) { // 2 argument
    Node head = oExp ;
    Node left = null ;
    Node right = null ;
    int level = 0 ;
    if ( oExp == null ) return null ; 
    else oExp = oExp.GetRightNode() ; // next
    
    Node temp = oExp ;
    while ( ! temp.GetData().equals( "nil" ) ) {
      if ( temp.GetLeftNode() != null ) level++ ;
      else if ( temp.GetData() != " " ) {
        System.out.print( "ERROR (non-list) : " ) ;
        PrettyPrint( head ) ;
        System.out.println( "" ) ;
        throw null ;
      } // else if
      
      temp = temp.GetRightNode() ;
    } // while
    
    if ( level < 2 || level > 2 ) {
      sErrorMsg = "ERROR (incorrect number of arguments) : cons" ;           
      throw null ;
    } // if
    
    // check format
    
    while ( oExp != null ) {
      
      if ( oExp.GetData().equals( "nil" ) ) {
        if ( left != null && right != null ) {
          head.SetLeft( left ) ;
          head.SetRight( right ) ;
          return head ;
        } // if
        
      } // if
      
      else if ( oExp.GetLeftNode() != null ) {
        if ( oExp.GetLeftNode().GetData() == " " ) {

        } // if
        
        if ( left == null ) left = EvalSExp( oExp.GetLeftNode() ) ;
        else if ( left != null && right == null ) right = EvalSExp( oExp.GetLeftNode() ) ; // second time 
      } // else if 
      
      oExp = oExp.GetRightNode() ;

    } // while
    
    return null ; // error
      
    
  } // CmdCons()
  
  public static Node CmdList( Node oExp ) {
    Node head = oExp ;

    boolean nil = false ;
    if ( oExp == null ) return null ; 
    else oExp = oExp.GetRightNode() ; // next
    
    Node temp = oExp ;
    while ( temp != null ) { // check format  
      if ( temp.GetData() != " " && ! temp.GetData().equals( "nil" ) ) {
        System.out.print( "ERROR (non-list) : " ) ;
        head.SetLeft( new Node( new Token( "list" ) ) );
        PrettyPrint( head ) ;
        System.out.println( "" ) ;
        throw null ;
      } // if
      
      temp = temp.GetRightNode() ;
    } // while
    // first loop is Evaluating every left Node  
    while ( oExp != null ) {
      if ( oExp.GetData().equals( "nil" ) ) nil = true ;
      else if ( oExp.GetLeftNode() != null ) oExp.SetLeft( EvalSExp( oExp.GetLeftNode() ) ) ;
      
      oExp = oExp.GetRightNode() ; // next
    } // while
    
    // second step is setting head
    if ( nil ) {
      head = head.GetRightNode() ;
      return head ;
    } // if
    
    return null ; // error 
  } // CmdList()
  
  public static Node CmdQuote( Node oExp ) {
    if ( oExp.GetRightNode() != null ) {
      if ( oExp.GetLeftNode() != null ) {
        return oExp.GetRightNode().GetLeftNode() ;
      } // if
      
      return null ;
      
    } // if
    
    else return null ;
  } // CmdQuote()
  
  public static void CmdDefine( Node oExp ) {
    Node head = oExp ;
    int level = 0 ;
    if ( oExp == null ) return ; 
    else oExp = oExp.GetRightNode() ; // next
    
    Node temp = oExp ;
    
    if ( temp.GetLeftNode().GetData() != " " ) {
      if ( temp.GetLeftNode() != null ) {
        if ( temp.GetLeftNode().GetData() == " " ) { // define symbol is not a tree
          System.out.print( "ERROR (DEFINE format) : " ) ;
          head.SetLeft( new Node( new Token( "define" ) ) ) ;
          PrettyPrint( head ) ;
          System.out.println( "" ) ;
          throw null ;
        } // if
        
      } // if
      
      while ( !temp.GetData().equals( "nil" ) ) {
        if ( temp.GetLeftNode() != null ) level++ ;
        temp = temp.GetRightNode() ;
      } // while
      
      
      if ( level < 2 || level > 2 ) {
        System.out.print( "ERROR (DEFINE format) : " ) ;  
        head.SetLeft( new Node( new Token( "define" ) ) ) ;
        PrettyPrint( head ) ;
        System.out.println( "" ) ;
        throw null ;
      } // if
      
      if ( FindFuntion( oExp.GetRightNode().GetLeftNode() ) != -1 ) 
        sFunction = true ;
      Node value = EvalSExp( oExp.GetRightNode().GetLeftNode() ) ;
      sFunction = false ;
      
      if ( !oExp.GetLeftNode().GetTokenType().equals( "SYMBOL" ) ) {  // error*******
        System.out.print( "ERROR (DEFINE format) : " ) ; 
        head.SetLeft( new Node( new Token( "define" ) ) ) ;
        PrettyPrint( head ) ;
        System.out.println( "" ) ;
        throw null ;
      } // if
      
      else {
        if ( FindEnviron( oExp.GetLeftNode() ) != -1 ) // arraylist exist
          sEnviron.set( FindEnviron( oExp.GetLeftNode() ), value ) ;
        else { // title define not yet
          
          if ( FindFuntion( oExp.GetRightNode().GetLeftNode() ) != -1 ) {
            sEnvironFun.add( oExp.GetLeftNode() ) ;
            sEnvironTitle.add( oExp.GetLeftNode() ) ;
            sEnviron.add( oExp.GetRightNode().GetLeftNode() ) ;
          } // if
          
          else {
            sEnvironTitle.add( oExp.GetLeftNode() ) ;
            sEnviron.add( value ) ;
          } // else
          
        } // else 
        
        sDefine = true ;
        System.out.println( oExp.GetLeftNode().GetData() + " defined" ) ;
        System.out.println( "" );
      } // else 
    } // if
    
    else { // define function
      Node title = temp.GetLeftNode().GetLeftNode() ; // title node
      if ( FindEnviron( title ) != -1 ) // arraylist exist
        sEnviron.set( FindEnviron( oExp.GetLeftNode().GetLeftNode() ), temp ) ;
      else { // title define not yet
        sEnvironTitle.add( title ) ;
        sEnviron.add( temp ) ;
        sEnvironFun.add( title ) ;
      } // else 
      
      sDefine = true ;
      System.out.println( title.GetData() + " defined" ) ;
      System.out.println( "" );
      
    } // else 
  } // CmdDefine()
  
  public static Node CmdCar( Node oExp ) { // 1 argument
    Node head = oExp ;
    int argu = 0 ;
    if ( oExp == null ) return null ; 
    else oExp = oExp.GetRightNode() ; // next
    Node temp = oExp ;
    while ( !temp.GetData().equals( "nil" ) ) { // check format
      if ( temp.GetLeftNode() != null ) argu++ ;
      temp = temp.GetRightNode() ;
    } // while
    
    if ( argu > 1 || argu < 1 ) {
      sErrorMsg = "ERROR (incorrect number of arguments) : car" ;           
      throw null ;
    } // if
    
    // first loop is Evaluating every left Node  
    while ( oExp != null ) {
      if ( oExp.GetLeftNode() != null ) {
        Node left = EvalSExp( oExp.GetLeftNode() ) ;
        if ( left.GetData() != " " ) { // atom not a tree
          sErrorMsg = "ERROR (car with incorrect argument type) : " + left.GetData() ;           
          throw null ;
        } // if
        
        oExp.SetLeft( left ) ;
      } // if
      // put left Node to Evaluate
      oExp = oExp.GetRightNode() ; // next
    } // while
    
    // Evaluate compete
    
    head = head.GetRightNode() ;
    head = head.GetLeftNode() ;
    if ( head.GetLeftNode() != null ) {
      if ( head.GetLeftNode().GetData() != " " ) sPrint = false ;
      return head.GetLeftNode() ;
    } // if
    
    return null ; // error
  } // CmdCar()
  
  public static Node CmdCdr( Node oExp ) {
    Node head = oExp ;
    int argu = 0 ;
    if ( oExp == null ) return null ; 
    else oExp = oExp.GetRightNode() ; // next
    
    Node temp = oExp ;
    while ( !temp.GetData().equals( "nil" ) ) { // check format
      if ( temp.GetLeftNode() != null ) argu++ ;
      temp = temp.GetRightNode() ;
    } // while
    
    if ( argu > 1 || argu < 1 ) {
      sErrorMsg = "ERROR (incorrect number of arguments) : cdr" ;           
      throw null ;
    } // if
    
    // first loop is Evaluating every left Node  
    while ( oExp != null ) {
      if ( oExp.GetLeftNode() != null ) {
        Node left = EvalSExp( oExp.GetLeftNode() ) ;
        if ( left.GetData() != " " ) { // atom not a tree
          sErrorMsg = "ERROR (cdr with incorrect argument type) : " + left.GetData() ;           
          throw null ;
        } // if
        
        oExp.SetLeft( left ) ;
      } // if
      // put left Node to Evaluate
      oExp = oExp.GetRightNode() ; // next
    } // while
    
    // Evaluate compete
    
    head = head.GetRightNode() ;
    head = head.GetLeftNode() ;
    if ( head.GetRightNode() != null ) {
      if ( head.GetRightNode().GetData() != " " ) sPrint = false ;
      return head.GetRightNode();
    } // if
    
    return null ; // error
  } // CmdCdr()
  
  public static void CmdClean() {
    sEnviron = new ArrayList <Node>() ;
    sEnvironTitle = new ArrayList <Node>() ;
    sEnvironFun = new ArrayList <Node>() ;
    System.out.println( "environment cleaned" );
    System.out.println( "" );
  } // CmdClean()
  
  public static Node CmdAtom( Node oExp ) { // 1 argument
    oExp = oExp.GetRightNode() ;
    int argu = 0 ;
    
    Node test = oExp ;
    while ( test != null ) { // check format
      if ( test.GetLeftNode() != null ) argu++ ;
      test = test.GetRightNode() ;
      if ( test.GetData().equals( "nil" ) ) test = null ;
    } // while
    
    if ( argu > 1 || argu < 1 ) {
      sErrorMsg = "ERROR (incorrect number of arguments) : atom?" ;           
      throw null ;
    } // if
    
    if ( oExp.GetLeftNode() != null ) {
      Node temp = EvalSExp( oExp.GetLeftNode() ) ;
      sPrint = false ;
      if ( temp.GetData() != " " ) {
        if ( temp.GetToken().GetAtom() )
          return new Node( new Token( "#t" ) ) ;
      } // if
      
      return new Node( new Token( "nil" ) ) ;
    } // if
    
    return null ; // error
  } // CmdAtom()
  
  public static Node CmdPair( Node oExp ) { // 1 argument
    oExp = oExp.GetRightNode() ;
    int argu = 0 ;
    
    Node test = oExp ;
    while ( !test.GetData().equals( "nil" ) ) { // check format
      if ( test.GetLeftNode() != null ) argu++ ;
      test = test.GetRightNode() ;
    } // while
    
    if ( argu > 1 || argu < 1 ) {
      sErrorMsg = "ERROR (incorrect number of arguments) : pair?" ;           
      throw null ;
    } // if
    
    if ( oExp.GetLeftNode() != null ) {
      Node temp = EvalSExp( oExp.GetLeftNode() ) ;
      sPrint = false ;
      if ( temp.GetData() == " " ) // is pair
        return new Node( new Token( "#t" ) ) ;
      else return new Node( new Token( "nil" ) ) ;
    } // if
    
    return null ; // error
  } // CmdPair()
  
  public static Node CmdListQ( Node oExp ) {
    oExp = oExp.GetRightNode() ;
    
    int argu = 0 ;
    
    Node test = oExp ;
    while ( !test.GetData().equals( "nil" ) ) { // check format
      if ( test.GetLeftNode() != null ) argu++ ;
      test = test.GetRightNode() ;
    } // while
    
    if ( argu > 1 || argu < 1 ) {
      sErrorMsg = "ERROR (incorrect number of arguments) : list?" ;           
      throw null ;
    } // if
    
    if ( oExp.GetLeftNode() != null ) {
      Node temp = EvalSExp( oExp.GetLeftNode() ) ;
      sPrint = false ;
      while ( temp != null ) {
        if ( temp.GetData().equals( "nil" ) ) return new Node( new Token( "#t" ) ) ; // ******
        temp = temp.GetRightNode() ;
      } // while

      return new Node( new Token( "nil" ) ) ;
    } // if
    
    return null ; // error
  } // CmdListQ()
  
  public static Node CmdNull( Node oExp ) {
    oExp = oExp.GetRightNode() ;
    
    int argu = 0 ;
    
    Node test = oExp ;
    while ( !test.GetData().equals( "nil" ) ) { // check format
      if ( test.GetLeftNode() != null ) argu++ ;
      test = test.GetRightNode() ;
    } // while
    
    if ( argu > 1 || argu < 1 ) {
      sErrorMsg = "ERROR (incorrect number of arguments) : null?" ;           
      throw null ;
    } // if
    
    if ( oExp.GetLeftNode() != null ) {
      Node temp = EvalSExp( oExp.GetLeftNode() ) ;
      sPrint = false ;
      while ( temp != null ) {
        if ( temp.GetData().equals( "nil" ) ) return new Node( new Token( "#t" ) ) ; // ******
        temp = temp.GetLeftNode() ;
      } // while

      return new Node( new Token( "nil" ) ) ;
    } // if
    
    return null ; // error
  } // CmdNull()
  
  public static Node CmdInt( Node oExp ) {
    oExp = oExp.GetRightNode() ;
    int argu = 0 ;
    
    Node test = oExp ;
    while ( !test.GetData().equals( "nil" ) ) { // check format
      if ( test.GetLeftNode() != null ) argu++ ;
      test = test.GetRightNode() ;
    } // while
    
    if ( argu > 1 || argu < 1 ) {
      sErrorMsg = "ERROR (incorrect number of arguments) : int?" ;           
      throw null ;
    } // if
    
    if ( oExp.GetLeftNode() != null ) {
      Node temp = EvalSExp( oExp.GetLeftNode() ) ;
      sPrint = false ;
      if ( temp.GetData() != " " ) {
        if ( temp.GetTokenType().equals( "INT" ) ) return new Node( new Token( "#t" ) ) ;
      } // if

      return new Node( new Token( "nil" ) ) ;
    } // if
    
    return null ; // error
  } // CmdInt()
  
  public static Node CmdReal( Node oExp ) {
    oExp = oExp.GetRightNode() ;
    int argu = 0 ;
    
    Node test = oExp ;
    while ( !test.GetData().equals( "nil" ) ) { // check format
      if ( test.GetLeftNode() != null ) argu++ ;
      test = test.GetRightNode() ;
    } // while
    
    if ( argu > 1 || argu < 1 ) {
      sErrorMsg = "ERROR (incorrect number of arguments) : real?" ;           
      throw null ;
    } // if
    
    if ( oExp.GetLeftNode() != null ) {
      Node temp = EvalSExp( oExp.GetLeftNode() ) ;
      sPrint = false ;
      if ( temp.GetData() != " " ) {
        if ( temp.GetTokenType().equals( "INT" ) || temp.GetTokenType().equals( "FLOAT" ) ) 
          return new Node( new Token( "#t" ) ) ;
      } // if

      return new Node( new Token( "nil" ) ) ;
    } // if
    
    return null ; // error
  } // CmdReal()
  
  public static Node CmdNumber( Node oExp ) {  
    oExp = oExp.GetRightNode() ;
    int argu = 0 ;
    
    Node test = oExp ;
    while ( !test.GetData().equals( "nil" ) ) { // check format
      if ( test.GetLeftNode() != null ) argu++ ;
      test = test.GetRightNode() ;
    } // while
    
    if ( argu > 1 || argu < 1 ) {
      sErrorMsg = "ERROR (incorrect number of arguments) : number?" ;           
      throw null ;
    } // if
    
    if ( oExp.GetLeftNode() != null ) {
      Node temp = EvalSExp( oExp.GetLeftNode() ) ;
      sPrint = false ;
      if ( temp.GetData() != " " ) {
        if ( temp.GetTokenType().equals( "INT" ) || temp.GetTokenType().equals( "FLOAT" ) ) 
          return new Node( new Token( "#t" ) ) ;
      } // if

      return new Node( new Token( "nil" ) ) ;
    } // if
    
    return null ; // error
  } // CmdNumber()
  
  public static Node CmdString( Node oExp ) {
    oExp = oExp.GetRightNode() ;
    int argu = 0 ;
    
    Node test = oExp ;
    while ( !test.GetData().equals( "nil" ) ) { // check format
      if ( test.GetLeftNode() != null ) argu++ ;
      test = test.GetRightNode() ;
    } // while
    
    if ( argu > 1 || argu < 1 ) {
      sErrorMsg = "ERROR (incorrect number of arguments) : string?" ;           
      throw null ;
    } // if
    
    if ( oExp.GetLeftNode() != null ) {
      Node temp = EvalSExp( oExp.GetLeftNode() ) ;
      sPrint = false ;
      if ( temp.GetData() != " " ) {
        if ( temp.GetTokenType().equals( "STRING" ) ) 
          return new Node( new Token( "#t" ) ) ;
      } // if

      return new Node( new Token( "nil" ) ) ;
    } // if
    
    return null ; // error
  } // CmdString()
  
  public static Node CmdBoolean( Node oExp ) {
    oExp = oExp.GetRightNode() ;
    int argu = 0 ;
    
    Node test = oExp ;
    while ( !test.GetData().equals( "nil" ) ) { // check format
      if ( test.GetLeftNode() != null ) argu++ ;
      test = test.GetRightNode() ;
    } // while
    
    if ( argu > 1 || argu < 1 ) {
      sErrorMsg = "ERROR (incorrect number of arguments) : boolean?" ;           
      throw null ;
    } // if
    
    if ( oExp.GetLeftNode() != null ) {
      Node temp = EvalSExp( oExp.GetLeftNode() ) ;
      sPrint = false ;
      if ( temp.GetData() != " " ) {
        if ( temp.GetData().equals( "#t" ) || temp.GetData().equals( "nil" ) ) 
          return new Node( new Token( "#t" ) ) ;
      } // if
      
      return new Node( new Token( "nil" ) ) ;
    } // if
    
    return null ; // error
    
  } // CmdBoolean()
  
  public static Node CmdSymbol( Node oExp ) {
    oExp = oExp.GetRightNode() ;
    int argu = 0 ;
    
    Node test = oExp ;
    while ( !test.GetData().equals( "nil" ) ) { // check format
      if ( test.GetLeftNode() != null ) argu++ ;
      test = test.GetRightNode() ;
    } // while
    
    if ( argu > 1 || argu < 1 ) {
      sErrorMsg = "ERROR (incorrect number of arguments) : symbol?" ;           
      throw null ;
    } // if
    
    if ( FindEnviron( oExp.GetLeftNode() ) != -1 ) return new Node( new Token( "nil" ) ) ;
    Node temp = EvalSExp( oExp.GetLeftNode() ) ;
    sPrint = false ;
    if ( temp.GetData() != " " ) {
      if ( temp.GetTokenType().equals( "SYMBOL" ) ) 
        return new Node( new Token( "#t" ) ) ; 
    } // if
      
    return new Node( new Token( "nil" ) ) ;
  } // CmdSymbol()
  
  public static Node CmdPlus( Node oExp ) { // more 2 argument
    boolean numf = false ;
    if ( oExp == null ) return null ; 
    else oExp = oExp.GetRightNode() ; // next
    // first loop is Evaluating every left Node
    Node temp = oExp ;
    int level = 0 ;
    while ( oExp != null ) {
      if ( oExp.GetLeftNode() != null ) {
        level++ ;
        Node left = EvalSExp( oExp.GetLeftNode() ) ;
        if ( left.GetTokenType().equals( "FLOAT" ) ) numf = true ;
        else if ( left.GetTokenType().equals( "INT" ) ) ;
        else { // error
          if ( left.GetData() != " " ) {
            sErrorMsg = "ERROR (+ with incorrect argument type) : " + left.GetData() ;             
          } // if
          
          else {
            System.out.print( "ERROR (+ with incorrect argument type) : " ) ;
            PrettyPrint( left )  ;
            System.out.println( "" ) ;
          } // else 
          
          throw null ;
        } // else 
        
        oExp.SetLeft( EvalSExp( left ) ) ;
      } // if
     
      oExp = oExp.GetRightNode() ; // next

    } // while
    
    if ( level < 2 ) { // error
      sErrorMsg = "ERROR (incorrect number of arguments) : +" ;           
      throw null ;
    } // if
    
    sPrint = false ;
    if ( !numf ) {
      int total = 0 ;
      while ( temp != null ) {        
        total = total + Integer.parseInt( temp.GetLeftNode().GetData() ) ;
     
        temp = temp.GetRightNode() ;
        if ( temp.GetData().equals( "nil" ) ) temp = null ;
      } // while
     
      return new Node( new Token( String.valueOf( total ) ) ) ;
    } // if
    
    else {
      float total = 0 ;
      while ( temp != null ) {        
        total = total + Float.parseFloat( temp.GetLeftNode().GetData() ) ;
     
        temp = temp.GetRightNode() ;
        if ( temp.GetData().equals( "nil" ) ) temp = null ;
      } // while
     
      return new Node( new Token( String.valueOf( total ) ) ) ;
    } // else 
    
    
  } // CmdPlus()
  
  public static Node CmdSub( Node oExp ) {
    boolean firstnum = false ;
    boolean numf = false ;
    if ( oExp == null ) return null ; 
    else oExp = oExp.GetRightNode() ; // next
    
    Node temp = oExp ;
    int level = 0 ;
    // first loop is Evaluating every left Node
    while ( oExp != null ) {
      if ( oExp.GetLeftNode() != null ) {
        level++;
        Node left = EvalSExp( oExp.GetLeftNode() ) ;
        if ( left.GetTokenType().equals( "FLOAT" ) ) numf = true ;
        else if ( left.GetTokenType().equals( "INT" ) ) ;
        else { // error
          if ( left.GetData() != " " ) {
            sErrorMsg = "ERROR (- with incorrect argument type) : " + left.GetData() ;           
          } // if
          
          else {
            System.out.print( "ERROR (- with incorrect argument type) : " ) ;
            PrettyPrint( left )  ;
            System.out.println( "" ) ;
          } // else 
          
          throw null ;
        } // else 
        
        oExp.SetLeft( EvalSExp( left ) ) ;
      } // if
     
      oExp = oExp.GetRightNode() ; // next

      
    } // while
    
    if ( level < 2 ) {
      sErrorMsg = "ERROR (incorrect number of arguments) : -" ;           
      throw null ;
    } // if
    
    sPrint = false ;
    if ( !numf ) {
      int total = 0 ;
      while ( temp != null ) {
        if ( !firstnum ) {
          total = total + Integer.parseInt( temp.GetLeftNode().GetData() ) ;
          firstnum = true ;
        } // if
      
        else 
          total = total - Integer.parseInt( temp.GetLeftNode().GetData() ) ;
        
        temp = temp.GetRightNode() ;
        if ( temp.GetData().equals( "nil" ) ) temp = null ;
      } // while
      
      return new Node( new Token( String.valueOf( total ) ) ) ;
    } // if
    
    else {
      float total = 0 ;
      
      while ( temp != null ) {
        if ( !firstnum ) {
          total = total + Float.parseFloat( temp.GetLeftNode().GetData() ) ;
          firstnum = true ;
        } // if
      
        else 
          total = total - Float.parseFloat( temp.GetLeftNode().GetData() ) ;
        
        temp = temp.GetRightNode() ;
        if ( temp.GetData().equals( "nil" ) ) temp = null ;
      } // while
      
      return new Node( new Token( String.valueOf( total ) ) ) ;
    } // else 
    
  } // CmdSub()
  
  public static Node CmdMult( Node oExp ) {
    boolean floatnum = false ;
    if ( oExp == null ) return null ; 
    else oExp = oExp.GetRightNode() ; // next
    // first loop is Evaluating every left Node
    Node temp = oExp ;
    int level = 0 ;
    while ( oExp != null ) {
      if ( oExp.GetLeftNode() != null ) {
        level++;
        Node leftnode = EvalSExp( oExp.GetLeftNode() ) ;
        if ( leftnode.GetTokenType().equals( "FLOAT" ) || leftnode.GetTokenType().equals( "INT" ) ) {
          if ( leftnode.GetTokenType().equals( "FLOAT" ) ) floatnum = true ; // the list have float
          oExp.SetLeft( leftnode ) ;
        } // if
        
        else { // error
          if ( leftnode.GetData() != " " ) {
            sErrorMsg = "ERROR (* with incorrect argument type) : " + leftnode.GetData() ;           
          } // if
          
          else {
            System.out.print( "ERROR (* with incorrect argument type) : " ) ;
            PrettyPrint( leftnode ) ;
            System.out.println( "" ) ;
          } // else 
          
          throw null ;
        } // else 
      } // if
     
      oExp = oExp.GetRightNode() ; // next

      
    } // while
    
    if ( level < 2 ) {
      sErrorMsg = "ERROR (incorrect number of arguments) : *" ;           
      throw null ;
    } // if
    
    sPrint = false ;
    
    if ( !floatnum ) {
      int total = 1 ;
      while ( temp != null ) {
        total = total * Integer.parseInt( temp.GetLeftNode().GetData() ) ;
        
        temp = temp.GetRightNode() ;
        if ( temp.GetData().equals( "nil" ) ) temp = null ;
      } // while
      
      return new Node( new Token( String.valueOf( total ) ) ) ;
    } // if
    
    else {
      float total = 1 ;
      while ( temp != null ) {
        total = total * Float.parseFloat( temp.GetLeftNode().GetData() ) ; 
    
        temp = temp.GetRightNode() ;
        if ( temp.GetData().equals( "nil" ) ) temp = null ;
      } // while
      
      return new Node( new Token( String.valueOf( total ) ) ) ;
    } // else 
    
    
  } // CmdMult()
  
  public static Node CmdDiv( Node oExp ) {
    boolean floatnum = false ;
    boolean firstnum = false ;
    if ( oExp == null ) return null ; 
    else oExp = oExp.GetRightNode() ; // next
    // first loop is Evaluating every left Node
    Node temp = oExp ;
    int level = 0 ;
    while ( oExp != null ) {
      if ( oExp.GetLeftNode() != null ) {
        level++;
        Node leftnode = EvalSExp( oExp.GetLeftNode() ) ;
        if ( leftnode.GetTokenType().equals( "FLOAT" ) || leftnode.GetTokenType().equals( "INT" ) ) {
          if ( leftnode.GetTokenType().equals( "FLOAT" ) ) {
            floatnum = true ; // the list have float
            if ( Float.parseFloat( leftnode.GetData() ) == 0 && level != 1 ) { // div zero error
              sErrorMsg = "ERROR (division by zero) : /" ;           
              throw null ;
            } // if
            
          } // if
          
          else if ( Integer.parseInt( leftnode.GetData() ) == 0 && level != 1 ) { // div zero error
            sErrorMsg = "ERROR (division by zero) : /" ;           
            throw null ;
          } // if
          
          oExp.SetLeft( leftnode ) ;
        } // if
        
        else { // error
          if ( leftnode.GetData() != " " ) {
            sErrorMsg = "ERROR (/ with incorrect argument type) : " + leftnode.GetData() ;           
          } // if
          
          else {
            System.out.print( "ERROR (/ with incorrect argument type) : " ) ;
            PrettyPrint( leftnode ) ;
            System.out.println( "" ) ;
          } // else
          
          throw null ;
        } // else 
      } // if
     
      oExp = oExp.GetRightNode() ; // next

      
    } // while
    
    if ( level < 2 ) {
      sErrorMsg = "ERROR (incorrect number of arguments) : /" ;           
      throw null ;
    } // if
    
    sPrint = false ;
    if ( !floatnum ) {
      int total = 0 ;
      while ( temp != null ) {
        
        if ( !firstnum ) {
          total = Integer.parseInt( temp.GetLeftNode().GetData() ) ;          
          firstnum = true ;
        } // if
               
        else total = total / Integer.parseInt( temp.GetLeftNode().GetData() ) ;
        
        temp = temp.GetRightNode() ;
        if ( temp.GetData().equals( "nil" ) ) temp = null ;
      } // while
      
      return new Node( new Token( String.valueOf( total ) ) ) ;
    } // if
    
    else {
      float total = 0 ;
      while ( temp != null ) {       
        if ( !firstnum ) {
          total = Float.parseFloat( temp.GetLeftNode().GetData() ) ;          
          firstnum = true ;
        } // if
        
        else 
          total = total / Float.parseFloat( temp.GetLeftNode().GetData() ) ; 
        
        
        temp = temp.GetRightNode() ;
        if ( temp.GetData().equals( "nil" ) ) temp = null ;
      } // while
      
      return new Node( new Token( String.valueOf( total ) ) ) ;
    } // else 
    
  } // CmdDiv() 
  
  public static Node CmdNot( Node oExp ) {
    oExp = oExp.GetRightNode() ;
    int argu = 0 ;
    
    Node test = oExp ;
    while ( !test.GetData().equals( "nil" ) ) { // check format
      if ( test.GetLeftNode() != null ) argu++ ;
      test = test.GetRightNode() ;
    } // while
    
    if ( argu > 1 || argu < 1 ) {
      sErrorMsg = "ERROR (incorrect number of arguments) : not" ;           
      throw null ;
    } // if
    
    if ( oExp.GetLeftNode() != null ) {
      Node temp = EvalSExp( oExp.GetLeftNode() ) ;
      sPrint = false ;
      while ( temp != null ) {
        if ( temp.GetData().equals( "nil" ) ) return new Node( new Token( "#t" ) ) ; // ******
        temp = temp.GetLeftNode() ;
      } // while

      return new Node( new Token( "nil" ) ) ;
    } // if
    
    return null ; // error 
  } // CmdNot()
  
  public static Node CmdAnd( Node oExp ) { // >= 2 argument
    oExp = oExp.GetRightNode() ;
    Node temp = oExp ;
    Node second = oExp ;
    Node bool = null ;
    Node last = null ;
    int argu = 0 ;
    while ( temp != null ) {
      if ( temp.GetLeftNode() != null )
        argu++ ;

      
      temp = temp.GetRightNode() ;

    } // while
    
    if ( argu < 2 ) {
      sErrorMsg = "ERROR (incorrect number of arguments) : and" ;           
      throw null ;
    } // if
    
    int now = 0 ;
    while ( second != null ) {
      if ( second.GetLeftNode() != null ) {
        now++ ;
        if ( now == 1 ) {
          bool = EvalSExp( second.GetLeftNode() ) ;
          if ( bool.GetData().equals( "nil" ) ) return bool ;
        } // if
        
        else {
          last = EvalSExp( second.GetLeftNode() ) ;
          if ( last.GetData().equals( "nil" ) ) return last ;
        } // else 
      } // if
      
      second = second.GetRightNode() ;
    } // while
    
    /*
    bool = EvalSExp( bool ) ;
    last = EvalSExp( last ) ;
    */
    
    return last ;
    
  } // CmdAnd()
  /*
  public static Node CmdOr( Node oExp ) {
    oExp = oExp.GetRightNode() ;
    Node temp = oExp ;
    Node bool = null ;
    Node last = null ;
    int argu = 0 ;
    while ( temp != null ) {
      if ( temp.GetLeftNode() != null ) {
        argu++ ;
        bool = EvalSExp( temp.GetLeftNode() ) ;
        if ( bool.GetData().equals( "#t" ) )
          last = bool ;
      } // if
      
      temp = temp.GetRightNode() ;

    } // while
    
    if ( argu < 2 ) {
      sErrorMsg = "ERROR (incorrect number of arguments) : and" ;           
      throw null ;
    } // if
    

    if ( last == null ) return new Node( new Token( "nil" ) ) ;
    else return last ;
  } // CmdOr()
  */
  
  public static Node CmdOr( Node oExp ) {
    oExp = oExp.GetRightNode() ;
    Node temp = oExp ;
    Node bool = null ;
    Node last = null ;
    int argu = 0 ;
    while ( temp != null ) {
      if ( temp.GetLeftNode() != null ) {
        argu++ ;
        bool = EvalSExp( temp.GetLeftNode() ) ;
        if ( ! bool.GetData().equals( "nil" ) )
          return bool ;
      } // if
      
      temp = temp.GetRightNode() ;

    } // while
    
    if ( argu < 2 ) {
      sErrorMsg = "ERROR (incorrect number of arguments) : and" ;           
      throw null ;
    } // if
    

    if ( last == null ) return new Node( new Token( "nil" ) ) ;
    else return null ;
  } // CmdOr()
  
  public static Node CmdBig( Node oExp ) {
    oExp = oExp.GetRightNode() ;
    Node temp = oExp ;
    boolean last = true ;
    boolean firstget = false ;
    int level = 0 ;
    float first = 0 ;
    float second = 0 ;
    while ( oExp != null ) {
      if ( oExp.GetLeftNode() != null ) {
        level++ ;
        Node temptwo = EvalSExp( oExp.GetLeftNode() ) ;
        boolean comone = temptwo.GetTokenType().equals( "FLOAT" ) ;
        boolean comtwo = temptwo.GetTokenType().equals( "INT" ) ;
        if ( ! comone && ! comtwo ) {
          if ( temptwo.GetData() != " " ) {
            sErrorMsg = "ERROR (> with incorrect argument type) : " + temptwo.GetData() ;           
          } // if
          
          else {
            System.out.print( "ERROR (> with incorrect argument type) : " ) ;
            PrettyPrint( temptwo ) ;
            System.out.println( "" ) ;
          } // else
          
          throw null ;
        } // if
        
        else oExp.SetLeft( temptwo ) ;
      } // if
      // put left Node to Evaluate
      oExp = oExp.GetRightNode() ; // next
      
    } // while
    
    if ( level < 2 ) {
      sErrorMsg = "ERROR (incorrect number of arguments) : >" ;           
      throw null ;
    } // if
    
    while ( temp != null ) {
      if ( temp.GetLeftNode() != null ) {
        if ( !firstget ) {
          firstget = true ;
          first = Float.parseFloat( temp.GetLeftNode().GetData() ) ;
        } // if
        
        else {
          second = Float.parseFloat( temp.GetLeftNode().GetData() ) ;
          if ( first > second ) ;
          else last = false ;
          
          first = second ;
        } // else 
          
      } // if
      
      
      else return null ;
      
      temp = temp.GetRightNode() ;
      if ( temp.GetData().equals( "nil" ) ) temp = null ;
    } // while
    
    sPrint = false ;
    if ( last ) return new Node( new Token( "#t" ) ) ;
    else return new Node( new Token( "nil" ) ) ;
  } // CmdBig()
  
  public static Node CmdBigEq( Node oExp ) {
    oExp = oExp.GetRightNode() ;
    Node temp = oExp ;
    boolean last = true ;
    boolean firstget = false ;
    int level = 0 ;
    float first = 0 ;
    float second = 0 ;
    while ( oExp != null ) {
      if ( oExp.GetLeftNode() != null ) {
        level++ ;
        Node temptwo = EvalSExp( oExp.GetLeftNode() ) ;
        boolean comone = temptwo.GetTokenType().equals( "FLOAT" ) ;
        boolean comtwo = temptwo.GetTokenType().equals( "INT" ) ;
        if ( ! comone && ! comtwo ) {
          if ( temptwo.GetData() != " " ) {
            sErrorMsg = "ERROR (>= with incorrect argument type) : " + temptwo.GetData() ;           
          } // if
          
          else {
            System.out.print( "ERROR (>= with incorrect argument type) : " ) ;
            PrettyPrint( temptwo ) ;
            System.out.println( "" ) ;
          } // else
          
          throw null ;
        } // if
        
        else oExp.SetLeft( temptwo ) ;
      } // if
      // put left Node to Evaluate
      oExp = oExp.GetRightNode() ; // next
    } // while
    
    if ( level < 2 ) {
      sErrorMsg = "ERROR (incorrect number of arguments) : >=" ;           
      throw null ;
    } // if
    
    while ( temp != null ) {
      if ( temp.GetLeftNode() != null ) {
        if ( !firstget ) {
          firstget = true ;
          first = Float.parseFloat( temp.GetLeftNode().GetData() ) ;
        } // if
        
        else {
          second = Float.parseFloat( temp.GetLeftNode().GetData() ) ;
          if ( first >= second ) ;
          else last = false ;
          
          first = second ;
        } // else 
          
      } // if
      
      
      else return null ;
      
      temp = temp.GetRightNode() ;
      if ( temp.GetData().equals( "nil" ) ) temp = null ;
    } // while
    
    sPrint = false ;
    if ( last ) return new Node( new Token( "#t" ) ) ;
    else return new Node( new Token( "nil" ) ) ;
  } // CmdBigEq()
  
  public static Node CmdSmall( Node oExp ) {
    oExp = oExp.GetRightNode() ;
    Node temp = oExp ;
    boolean last = true ;
    boolean firstget = false ;
    int level = 0 ;
    float first = 0 ;
    float second = 0 ;
    while ( oExp != null ) {
      if ( oExp.GetLeftNode() != null ) {
        level++;
        Node temptwo = EvalSExp( oExp.GetLeftNode() ) ;
        boolean comone = temptwo.GetTokenType().equals( "FLOAT" ) ;
        boolean comtwo = temptwo.GetTokenType().equals( "INT" ) ;
        if ( ! comone && ! comtwo ) {
          if ( temptwo.GetData() != " " ) {
            sErrorMsg = "ERROR (< with incorrect argument type) : " + temptwo.GetData() ;           
          } // if
          
          else {
            System.out.print( "ERROR (< with incorrect argument type) : " ) ;
            PrettyPrint( temptwo ) ;
            System.out.println( "" ) ;
          } // else
          
          throw null ;
        } // if
        
        else oExp.SetLeft( temptwo ) ;
      } // if
      // put left Node to Evaluate
      oExp = oExp.GetRightNode() ; // next
      
    } // while
    
    if ( level < 2 ) {
      sErrorMsg = "ERROR (incorrect number of arguments) : <" ;           
      throw null ;
    } // if
    
    while ( temp != null ) {
      if ( temp.GetLeftNode() != null ) {
        if ( !firstget ) {
          firstget = true ;
          first = Float.parseFloat( temp.GetLeftNode().GetData() ) ;
        } // if
        
        else {
          second = Float.parseFloat( temp.GetLeftNode().GetData() ) ;
          if ( first < second ) ;
          else last = false ;
          
          first = second ;
        } // else 
          
      } // if
      
      
      else return null ;
      
      temp = temp.GetRightNode() ;
      if ( temp.GetData().equals( "nil" ) ) temp = null ;
    } // while
    
    sPrint = false ;
    if ( last ) return new Node( new Token( "#t" ) ) ;
    else return new Node( new Token( "nil" ) ) ;
  } // CmdSmall()
  
  public static Node CmdSmallEq( Node oExp ) {
    oExp = oExp.GetRightNode() ;
    Node temp = oExp ;
    boolean last = true ;
    boolean firstget = false ;
    int level = 0 ;
    float first = 0 ;
    float second = 0 ;
    while ( oExp != null ) {
      if ( oExp.GetLeftNode() != null ) {
        level++ ;
        Node temptwo = EvalSExp( oExp.GetLeftNode() ) ;
        boolean comone = temptwo.GetTokenType().equals( "FLOAT" ) ;
        boolean comtwo = temptwo.GetTokenType().equals( "INT" ) ;
        if ( ! comone && ! comtwo ) {
          if ( temptwo.GetData() != " " ) {
            sErrorMsg = "ERROR (<= with incorrect argument type) : " + temptwo.GetData() ;           
          } // if
          
          else {
            System.out.print( "ERROR (<= with incorrect argument type) : " ) ;
            PrettyPrint( temptwo ) ;
            System.out.println( "" ) ;
          } // else
          
          throw null ;
        } // if
        
        else oExp.SetLeft( temptwo ) ;
      } // if
      // put left Node to Evaluate
      oExp = oExp.GetRightNode() ; // next
      
    } // while
    
    if ( level < 2 ) {
      sErrorMsg = "ERROR (incorrect number of arguments) : <=" ;           
      throw null ;
    } // if
    
    while ( temp != null ) {
      if ( temp.GetLeftNode() != null ) {
        if ( !firstget ) {
          firstget = true ;
          first = Float.parseFloat( temp.GetLeftNode().GetData() ) ;
        } // if
        
        else {
          second = Float.parseFloat( temp.GetLeftNode().GetData() ) ;
          if ( first <= second ) ;
          else last = false ;
          
          first = second ;
        } // else 
          
      } // if
      
      
      else return null ;
      
      temp = temp.GetRightNode() ;
      if ( temp.GetData().equals( "nil" ) ) temp = null ;
    } // while
    
    sPrint = false ;
    if ( last ) return new Node( new Token( "#t" ) ) ;
    else return new Node( new Token( "nil" ) ) ;
  } // CmdSmallEq()
  
  public static Node CmdEq( Node oExp ) {
    oExp = oExp.GetRightNode() ;
    Node temp = oExp ;
    boolean last = true ;
    boolean firstget = false ;
    int level = 0 ;
    float first = 0 ;
    float second = 0 ;
    while ( oExp != null ) {
      if ( oExp.GetLeftNode() != null ) {
        level++ ;
        Node temptwo = EvalSExp( oExp.GetLeftNode() ) ;
        boolean comone = temptwo.GetTokenType().equals( "FLOAT" ) ;
        boolean comtwo = temptwo.GetTokenType().equals( "INT" ) ;
        if ( ! comone && ! comtwo ) {
          if ( temptwo.GetData() != " " ) {
            sErrorMsg = "ERROR (= with incorrect argument type) : " + temptwo.GetData() ;           
          } // if
          
          else {
            System.out.print( "ERROR (= with incorrect argument type) : " ) ;
            PrettyPrint( temptwo ) ;
            System.out.println( "" ) ;
          } // else
          
          throw null ;
        } // if
        
        else oExp.SetLeft( temptwo ) ;
      } // if
      // put left Node to Evaluate
      oExp = oExp.GetRightNode() ; // next
      
    } // while
    
    if ( level < 2 ) {
      sErrorMsg = "ERROR (incorrect number of arguments) : =" ;           
      throw null ;
    } // if
    
    while ( temp != null ) {
      if ( temp.GetLeftNode() != null ) {
        if ( !firstget ) {
          firstget = true ;
          first = Float.parseFloat( temp.GetLeftNode().GetData() ) ;
        } // if
        
        else {
          second = Float.parseFloat( temp.GetLeftNode().GetData() ) ;
          if ( first == second ) ;
          else last = false ;
          
          first = second ;
        } // else 
          
      } // if
      
      
      else return null ;
      
      temp = temp.GetRightNode() ;
      if ( temp.GetData().equals( "nil" ) ) temp = null ;
    } // while
    
    sPrint = false ;
    if ( last ) return new Node( new Token( "#t" ) ) ;
    else return new Node( new Token( "nil" ) ) ;
  } // CmdEq()
  
  public static Node CmdStringAppend( Node oExp ) { // 2argument
    oExp = oExp.GetRightNode() ;
    Node temp = oExp ;
    int level = 0 ;
    String target = "" ;
    while ( oExp != null ) {
      if ( oExp.GetLeftNode() != null ) {
        level++ ;
        Node temptwo = EvalSExp( oExp.GetLeftNode() ) ;
        if ( ! temptwo.GetTokenType().equals( "STRING" ) ) {
          if ( temptwo.GetData() != " " ) {
            sErrorMsg = "ERROR (string-append with incorrect argument type) : " 
                        + temptwo.GetData() ;           
          } // if
          
          else {
            System.out.print( "ERROR (string-append with incorrect argument type) : " ) ;
            PrettyPrint( temptwo ) ;
            System.out.println( "" ) ;
          } // else
          
          throw null ;
        } // if
        
        else {
          target = target + temptwo.GetData().substring( 1, temptwo.GetData().length() - 1 ) ;
          oExp.SetLeft( temptwo ) ;
        } // else 
        
      } // if
      // put left Node to Evaluate
      oExp = oExp.GetRightNode() ; // next
      
    } // while
    
    if ( level < 2 ) {
      sErrorMsg = "ERROR (incorrect number of arguments) : string-append" ;           
      throw null ;
    } // if
    
    sPrint = false ;
    return new Node( new Token( '"' + target + '"' ) ) ;
    
  } // CmdStringAppend()
  
  public static Node CmdStringBig( Node oExp ) {
    oExp = oExp.GetRightNode() ;
    Node temp = oExp ;
    int level = 0 ;
    boolean last = true ;
    boolean first = false ;
    String target = "" ;
    while ( oExp != null ) {
      if ( oExp.GetLeftNode() != null ) {
        level++ ;
        Node temptwo = EvalSExp( oExp.GetLeftNode() ) ;
        if ( ! temptwo.GetTokenType().equals( "STRING" ) ) {
          if ( temptwo.GetData() != " " ) {
            sErrorMsg = "ERROR (string>? with incorrect argument type) : " + temptwo.GetData() ;           
          } // if
          
          else {
            System.out.print( "ERROR (string>? with incorrect argument type) : " ) ;
            PrettyPrint( temptwo ) ;
            System.out.println( "" ) ;
          } // else
          
          throw null ;
        } // if
        
        else 
          oExp.SetLeft( temptwo ) ;

      } // if
      // put left Node to Evaluate
      oExp = oExp.GetRightNode() ; // next
      
    } // while
    
    if ( level < 2 ) {
      sErrorMsg = "ERROR (incorrect number of arguments) : string>?" ;           
      throw null ;
    } // if
    
    while ( temp != null ) {
      if ( temp.GetLeftNode() != null ) {
        if ( !first ) {
          first = true ;
          target = temp.GetLeftNode().GetData() ; // put in
        } // if
        
        else {
          if ( target.compareTo( temp.GetLeftNode().GetData() ) > 0 ) ;
          else last = false ;
          
          target = temp.GetLeftNode().GetData() ;
        } // else 
          
      } // if
      
      
      else return null ;
      
      temp = temp.GetRightNode() ;
      if ( temp.GetData().equals( "nil" ) ) temp = null ;
    } // while 
    
    sPrint = false ;
    if ( last ) return new Node( new Token( "#t" ) ) ;
    else return new Node( new Token( "nil" ) ) ;
  } // CmdStringBig()
  
  public static Node CmdStringSmall( Node oExp ) {
    oExp = oExp.GetRightNode() ;
    Node temp = oExp ;
    int level = 0 ;
    boolean last = true ;
    boolean first = false ;
    String target = "" ;
    while ( oExp != null ) {
      if ( oExp.GetLeftNode() != null ) {
        level++ ;
        Node temptwo = EvalSExp( oExp.GetLeftNode() ) ;
        if ( ! temptwo.GetTokenType().equals( "STRING" ) ) {
          if ( temptwo.GetData() != " " ) {
            sErrorMsg = "ERROR (string<? with incorrect argument type) : " + temptwo.GetData() ;           
          } // if
          
          else {
            System.out.print( "ERROR (string<? with incorrect argument type) : " ) ;
            PrettyPrint( temptwo ) ;
            System.out.println( "" ) ;
          } // else
          
          throw null ;
        } // if
        
        else 
          oExp.SetLeft( temptwo ) ;

      } // if
      // put left Node to Evaluate
      oExp = oExp.GetRightNode() ; // next
      
    } // while
    
    if ( level < 2 ) {
      sErrorMsg = "ERROR (incorrect number of arguments) : string<?" ;           
      throw null ;
    } // if
    
    while ( temp != null ) {
      if ( temp.GetLeftNode() != null ) {
        if ( !first ) {
          first = true ;
          target = temp.GetLeftNode().GetData() ; // put in
        } // if
        
        else {
          if ( target.compareTo( temp.GetLeftNode().GetData() ) < 0 ) ;
          else last = false ;
          
          target = temp.GetLeftNode().GetData() ;
        } // else 
          
      } // if
      
      
      else return null ;
      
      temp = temp.GetRightNode() ;
      if ( temp.GetData().equals( "nil" ) ) temp = null ;
    } // while 
    
    sPrint = false ;
    if ( last ) return new Node( new Token( "#t" ) ) ;
    else return new Node( new Token( "nil" ) ) ;
  } // CmdStringSmall()
  
  public static Node CmdStringEq( Node oExp ) {
    oExp = oExp.GetRightNode() ;
    Node temp = oExp ;
    int level = 0 ;
    boolean last = true ;
    boolean first = false ;
    String target = "" ;
    while ( oExp != null ) {
      if ( oExp.GetLeftNode() != null ) {
        level++ ;
        Node temptwo = EvalSExp( oExp.GetLeftNode() ) ;
        if ( ! temptwo.GetTokenType().equals( "STRING" ) ) {
          if ( temptwo.GetData() != " " ) {
            sErrorMsg = "ERROR (string=? with incorrect argument type) : " + temptwo.GetData() ;           
          } // if
          
          else {
            System.out.print( "ERROR (string=? with incorrect argument type) : " ) ;
            PrettyPrint( temptwo ) ;
            System.out.println( "" ) ;
          } // else
          
          throw null ;
        } // if
        
        else 
          oExp.SetLeft( temptwo ) ;

      } // if
      // put left Node to Evaluate
      oExp = oExp.GetRightNode() ; // next
      
      
    } // while
    
    if ( level < 2 ) {
      sErrorMsg = "ERROR (incorrect number of arguments) : string=?" ;           
      throw null ;
    } // if
    
    while ( temp != null ) {
      if ( temp.GetLeftNode() != null ) {
        if ( !first ) {
          first = true ;
          target = temp.GetLeftNode().GetData() ; // put in
        } // if
        
        else {
          if ( target.compareTo( temp.GetLeftNode().GetData() ) == 0 ) ;
          else last = false ;
          
          target = temp.GetLeftNode().GetData() ;
        } // else 
          
      } // if
      
      
      else return null ;
      
      temp = temp.GetRightNode() ;
      if ( temp.GetData().equals( "nil" ) ) temp = null ;
    } // while 
    
    sPrint = false ;
    if ( last ) return new Node( new Token( "#t" ) ) ;
    else return new Node( new Token( "nil" ) ) ;
  } // CmdStringEq()
  
  public static Node CmdEqv( Node oExp ) { // 2 argument
    oExp = oExp.GetRightNode() ;
    int level = 0 ;
    Node first = null ;
    Node second = null ;
    Node temp = oExp ;
    while ( temp != null ) {
      if ( temp.GetLeftNode() != null )
        level++ ;
      
      temp = temp.GetRightNode() ;
      
      
    } // while
    
    if ( level != 2 ) {
      sErrorMsg = "ERROR (incorrect number of arguments) : eqv?" ;           
      throw null ;
    } // if
    
    first = EvalSExp( oExp.GetLeftNode() ) ;
    oExp = oExp.GetRightNode() ; // next
    second = EvalSExp( oExp.GetLeftNode() ) ;
    if ( first == second ) return new Node( new Token( "#t" ) ) ; // same Node
    else if ( first.GetTokenType().equals( "STRING" ) && second.GetTokenType().equals( "STRING" ) )
      return new Node( new Token( "nil" ) ) ;
    else {
      if ( first.GetData() != " " && second.GetData() != " " ) { // atom
        if ( first.GetData().equals( second.GetData() ) ) return new Node( new Token( "#t" ) ) ;
      } // if
      
      return new Node( new Token( "nil" ) ) ;
    } // else
    
  } // CmdEqv()
  
  public static Node CmdEqual( Node oExp ) {
    oExp = oExp.GetRightNode() ;
    int level = 0 ;
    Node first = null ;
    Node second = null ;
    Node temp = oExp ;
    while ( temp != null ) {
      if ( temp.GetLeftNode() != null )
        level++ ;
      
      temp = temp.GetRightNode() ;
      
    } // while
    
    if ( level != 2 ) {
      sErrorMsg = "ERROR (incorrect number of arguments) : equal?" ;           
      throw null ;
    } // if
    

    first = EvalSExp( oExp.GetLeftNode() ) ;
    oExp = oExp.GetRightNode() ;
    second = EvalSExp( oExp.GetLeftNode() ) ;
    sPrint = false ;
    if ( Compare( first, second ) ) return new Node( new Token( "#t" ) ) ; 
    else return new Node( new Token( "nil" ) ) ;

  } // CmdEqual()
  
  public static boolean Compare( Node one, Node two ) {
    if ( one.GetData() != " " && two.GetData() != " " ) { // atom
      if ( one.GetData().equals( two.GetData() ) ) return true ;
      else  return false ;
    } // if
    
    while ( one != null && two != null ) {
      if ( one.GetLeftNode() == null || two.GetLeftNode() == null ) return false ; 
      if ( ! Compare( one.GetLeftNode(), two.GetLeftNode() ) ) return false ;
      one = one.GetRightNode() ;
      two = two.GetRightNode() ;
      if ( one.GetData().equals( "nil" ) ) one = null ;
      if ( two.GetData().equals( "nil" ) ) two = null ;
    } // while
    
    return true ;
  } // Compare()
  
  public static Node CmdBegin( Node oExp ) {
    oExp = oExp.GetRightNode() ;
    int level = 0 ;
    Node last = null ;
    while ( oExp != null ) {
      if ( oExp.GetLeftNode() != null ) {
        level++ ;
        last = EvalSExp( oExp.GetLeftNode() ) ;
      } // if
      
      oExp = oExp.GetRightNode() ;
    } // while
    
    if ( level < 1 ) {
      sErrorMsg = "ERROR (incorrect number of arguments) : begin" ;           
      throw null ;
    } // if
    
    else {
      return last ;
    } // else
    
  } // CmdBegin()
  
  public static Node CopyNode( Node oExp ) {
    Node temp = new Node() ;
    Node head = oExp ;
    Node top = temp ;
    while ( temp != null ) {
      if ( head.GetLeftNode() != null ) {
        if ( head.GetLeftNode().GetData() != " " )
          temp.SetLeft( new Node( new Token( head.GetLeftNode().GetData() ) ) ) ;
        else 
          temp.SetLeft( CopyNode( head.GetLeftNode() ) ) ;
      } // if
      
      if ( head.GetRightNode() != null ) 
        temp.SetRight( new Node( new Token( head.GetRightNode().GetData() ) ) ) ;
      temp = temp.GetRightNode() ;
      head = head.GetRightNode() ;
    } // while
    
    return top ;
  } // CopyNode()
  
  public static Node CmdIf( Node oExp ) { // 2 or 3 argument
    Node error = CopyNode( oExp ) ;
    oExp = oExp.GetRightNode() ;
    Node temp = oExp ;
    int argu = 0 ;
    Node bool = null ;
    Node tnode = null ;
    Node fnode = null ;
    
    // check argument
    while ( temp != null ) {
      if ( temp.GetLeftNode() != null ) {
        if ( argu == 0 ) bool = temp.GetLeftNode() ;
        else if ( argu == 1 ) tnode = temp.GetLeftNode() ;
        else if ( argu == 2 ) fnode = temp.GetLeftNode() ;
        argu++ ;
      } // if
    
      temp = temp.GetRightNode() ;
    } // while
    
    if ( argu != 2 && argu != 3 ) {
      sErrorMsg = "ERROR (incorrect number of arguments) : if" ;           
      throw null ;
    } // if
    
    bool = EvalSExp( bool ) ;
    // if ( ! bool.GetData().equals( "nil" ) ) tnode = EvalSExp( tnode ) ;
    // if ( fnode != null ) fnode = EvalSExp( fnode ) ; 
    if ( bool.GetData().equals( "nil" ) ) {
      if ( fnode == null ) {
        sPrint = true ;
        System.out.print( "ERROR (no return value) : " ) ; 
        error.SetLeft( new Node( new Token( "if" ) ) ) ;
        PrettyPrint( error ) ;
        System.out.println( "" ) ;
        throw null ;
      } // if
      
      else return EvalSExp( fnode ) ;
    } // if
    
    else 
      return EvalSExp( tnode ) ; 

  } // CmdIf()
  
  public static Node CmdCond( Node oExp ) {  // >= 1 argument
    Node error = CopyNode( oExp ) ;
    oExp = oExp.GetRightNode() ;
    Node temp = oExp ;
    int argu = 0 ;
    Node bool = null ;
    Node tnode = null ;
    // Node target = null ;
    while ( temp != null ) {
      if ( temp.GetLeftNode() != null ) {
        Node left = temp.GetLeftNode() ;
        
        if ( left.GetLeftNode() != null && left.GetRightNode() != null ) {
          if ( left.GetRightNode().GetData() == " " )
            argu++ ;
          else {
            sPrint = true ;
            System.out.print( "ERROR (COND format) : " ) ;
            error.SetLeft( new Node( new Token( "cond" ) ) ) ;
            PrettyPrint( error ) ;
            System.out.println( "" ) ;
            throw null ;
          } // else 
        } // if
        
        else if ( left.GetLeftNode() == null && left.GetRightNode() == null ) { // error
          // the node is atom
          // not boolean and exp
          sPrint = true ;
          System.out.print( "ERROR (COND format) : " ) ;
          error.SetLeft( new Node( new Token( "cond" ) ) ) ;
          PrettyPrint( error ) ;
          System.out.println( "" ) ;
          throw null ;
        } // else if
      } // if
      
      else { // 
        sPrint = true ;
        System.out.print( "ERROR (COND format) : " ) ;
        error.SetLeft( new Node( new Token( "cond" ) ) ) ;
        PrettyPrint( error ) ;
        System.out.println( "" ) ;
        throw null ;
      } // else 
      
      temp = temp.GetRightNode() ;
      if ( temp.GetData().equals( "nil" ) ) temp = null ;
    } // while
    
    if ( argu < 1 ) { // argument error
      sPrint = true ;
      System.out.print( "ERROR (COND format) : " ) ;
      error.SetLeft( new Node( new Token( "cond" ) ) ) ;
      PrettyPrint( error ) ;
      System.out.println( "" ) ;
      throw null ;
    } // if
    
    
    int now = 0 ;
    while ( oExp != null ) {
      now++ ;
      Node left = oExp.GetLeftNode() ;

      if ( now == argu && left.GetLeftNode().GetData().equals( "else" ) ) {
        bool = left.GetLeftNode() ;
        left = left.GetRightNode() ;
      } // if
      
      else 
        bool = EvalSExp( left.GetLeftNode() ) ;
      
      if ( ! bool.GetData().equals( "nil" ) ) {

        while ( left != null ) {
          if ( left.GetLeftNode() != null ) 
            tnode = EvalSExp( left.GetLeftNode() ) ;
          
          left = left.GetRightNode() ;
        } // while
        
        if ( tnode != null ) return tnode ;
        else {
          sPrint = true ;
          System.out.print( "ERROR (COND format) : " ) ;
          error.SetLeft( new Node( new Token( "cond" ) ) ) ;
          PrettyPrint( error ) ;
          System.out.println( "" ) ;
          throw null ;
        } // else 
        
      } // if
         
      
      oExp = oExp.GetRightNode() ;
      if ( oExp.GetData().equals( "nil" ) ) oExp = null ;
    } // while
    
    sPrint = true ;
    System.out.print( "ERROR (no return value) : " ) ; 
    error.SetLeft( new Node( new Token( "cond" ) ) ) ;
    PrettyPrint( error ) ;
    System.out.println( "" ) ;
    throw null ;
  } // CmdCond()
  
  public static Node CmdLet( Node oExp ) {
    oExp = oExp.GetRightNode() ;
    Node define = oExp.GetLeftNode() ;
    int size = sEnviron.size() ;
    // define first Node
    ArrayList<Node> temptitle = new ArrayList <Node>() ;
    ArrayList<Node> tempenviron = new ArrayList <Node>() ;
    
    int level = 0 ;
    while ( ! define.GetData().equals( "nil" ) ) {
      Node temp = define.GetLeftNode() ;
      Node var = temp.GetLeftNode() ;
      temp = temp.GetRightNode() ;
      Node exp = null ;
      // deal exp
      if ( temp.GetLeftNode() != null ) {
        if ( temp.GetLeftNode().GetTokenType().equals( "cmd" ) )
          exp = EvalSExp( temp ) ;
        else if ( FindFuntion( temp.GetLeftNode() ) != -1 ) 
          exp = EvalSExp( temp ) ;
        
        
        else 
          exp = EvalSExp( temp.GetLeftNode() ) ; // left is atom not a command
        
      } // if
      
      // set in array list
      if ( var != null && exp != null ) {
        temptitle.add( var ) ;
        tempenviron.add( exp ) ;
      } // if
      
      define = define.GetRightNode() ;
      level++ ;
    } // while
    
    for ( int i = 0 ; i < level ; i++ ) {
      sEnvironTitle.add( temptitle.get( i ) ) ;
      sEnviron.add( tempenviron.get( i ) ) ;
    } // for
    
    
    int num = sEnviron.size() - size ;
    oExp = oExp.GetRightNode() ;
    // ***********return value******************
    Node target = null ;
    while ( ! oExp.GetData().equals( "nil" ) ) {
      target = EvalSExp( oExp.GetLeftNode() ) ;
      oExp = oExp.GetRightNode() ;
    } // while
    
    // **********delete arraylist************
    while ( num != 0 ) {
      sEnvironTitle.remove( sEnvironTitle.size() - 1 );
      sEnviron.remove( sEnviron.size() - 1 );
      num-- ;
    } // while
    
    return target ;

  } // CmdLet()
  
  public static Node CmdLambda( Node oExp ) {
    Node copy = CopyNode( oExp ) ;
    // set lambda to procedure
    oExp.SetLeft( new Node( new Token( "#<procedure " + oExp.GetLeftNode().GetData() + ">" ) ) ) ;
    // -----
   
    return oExp ;
  } // CmdLambda()
  
  
  public static Node ExeLambda( Node oExp ) {
    Node copy = CopyNode( oExp ) ;
    Node left = oExp.GetLeftNode().GetRightNode().GetLeftNode() ;
    Node right = oExp.GetRightNode() ;
    
    ArrayList<Node> temptitle = new ArrayList <Node>() ;
    ArrayList<Node> tempenviron = new ArrayList <Node>() ;
    
    int level = 0 ;
    Node target = null ;
    Node title = null ;
    Node exp = null ;
    while ( ! left.GetData().equals( "nil" ) ) {
      title = left.GetLeftNode() ;
      exp = EvalSExp( right.GetLeftNode() ) ;
      temptitle.add( title ) ;
      tempenviron.add( exp ) ;
      
      left = left.GetRightNode() ;
      right = right.GetRightNode() ;
      level++ ;
    } // while
    
    for ( int i = 0 ; i < level ; i++ ) {
      sEnvironTitle.add( temptitle.get( i ) ) ;
      sEnviron.add( tempenviron.get( i ) ) ;
    } // for
    
    
    copy = copy.GetLeftNode().GetRightNode().GetRightNode() ;
    while ( ! copy.GetData().equals( "nil" ) ) {
      target = EvalSExp( copy.GetLeftNode() ) ;
      copy = copy.GetRightNode() ;
    } // while 
    
    while ( level != 0 ) {
      sEnvironTitle.remove( sEnvironTitle.size() - 1 );
      sEnviron.remove( sEnviron.size() - 1 );
      level-- ;
    } // while
    
    return target ;

  } // ExeLambda()
  
  public static boolean CheckCommand( Node oExp ) {
    if ( oExp.GetData().equals( "#<procedure exit>" ) ) return true ;
    if ( oExp.GetData().equals( "#<procedure cons>" ) ) return true ;
    if ( oExp.GetData().equals( "#<procedure list>" ) ) return true ;
    
    if ( oExp.GetData().equals( "#<procedure quote>" ) ) return true ;
    
    if ( oExp.GetData().equals( "#<procedure define>" ) ) return true ;
    if ( oExp.GetData().equals( "#<procedure car>" ) ) return true ;
    if ( oExp.GetData().equals( "#<procedure cdr>" ) ) return true ;
    
    if ( oExp.GetData().equals( "#<procedure atom?>" ) ) return true ;
    if ( oExp.GetData().equals( "#<procedure pair?>" ) ) return true ;
    if ( oExp.GetData().equals( "#<procedure list?>" ) ) return true ;
    if ( oExp.GetData().equals( "#<procedure null?>" ) ) return true ;
    if ( oExp.GetData().equals( "#<procedure integer?>" ) ) return true ;
    if ( oExp.GetData().equals( "#<procedure real?>" ) ) return true ;
    if ( oExp.GetData().equals( "#<procedure number?>" ) ) return true ;
    if ( oExp.GetData().equals( "#<procedure string?>" ) ) return true ;
    if ( oExp.GetData().equals( "#<procedure boolean?>" ) ) return true ;
    if ( oExp.GetData().equals( "#<procedure symbol?>" ) ) return true ;
    
    if ( oExp.GetData().equals( "#<procedure +>" ) ) return true ;
    if ( oExp.GetData().equals( "#<procedure ->" ) ) return true ;
    if ( oExp.GetData().equals( "#<procedure *>" ) ) return true ;
    if ( oExp.GetData().equals( "#<procedure />" ) ) return true ;
    
    if ( oExp.GetData().equals( "#<procedure not>" ) ) return true ;
    if ( oExp.GetData().equals( "#<procedure and>" ) ) return true ;
    if ( oExp.GetData().equals( "#<procedure or>" ) ) return true ;
    
    if ( oExp.GetData().equals( "#<procedure >>" ) ) return true ;
    if ( oExp.GetData().equals( "#<procedure >=>" ) ) return true ;
    if ( oExp.GetData().equals( "#<procedure <>" ) ) return true ;
    if ( oExp.GetData().equals( "#<procedure <=>" ) ) return true ;
    if ( oExp.GetData().equals( "#<procedure =>" ) ) return true ;
    if ( oExp.GetData().equals( "#<procedure string-append>" ) ) return true ;
    if ( oExp.GetData().equals( "#<procedure string>?>" ) ) return true ;
    if ( oExp.GetData().equals( "#<procedure string<?>" ) ) return true ;
    if ( oExp.GetData().equals( "#<procedure string=?>" ) ) return true ;
    
    if ( oExp.GetData().equals( "#<procedure eqv?>" ) ) return true ;
    if ( oExp.GetData().equals( "#<procedure equal?>" ) ) return true ;
    
    if ( oExp.GetData().equals( "#<procedure begin>" ) ) return true ; 
    
    if ( oExp.GetData().equals( "#<procedure if>" ) ) return true ;
    if ( oExp.GetData().equals( "#<procedure cond>" ) ) return true ;
    
    if ( oExp.GetData().equals( "#<procedure clean-environment>" ) ) return true ;
    
    if ( oExp.GetData().equals( "#<procedure let>" ) ) return true ;
    if ( oExp.GetData().equals( "#<procedure lambda>" ) ) return true ;
    
    
    return false ;
  } // CheckCommand()
  
  public static void PrettyPrint( Node root ) {
    if ( sDefine ) {
      sDefine = false ;
      return ;
    } // if
    
    if ( root != null &&  sPrint ) {
      if ( root.GetData().equals( "nil" )  ) {
        root.Printf();
        return ;
      } // if 
 
      boolean first = false ;

      if ( !sLeftTree ) System.out.print( "( " ) ;
      else if ( !sExist && sLeftTree )  System.out.print( "( " ) ;
      
      else {
        System.out.print( sSpace + "( " ) ;
        sExist = false ;
      } // else 
      
      sLP = false ;
      while ( root != null ) {
        if ( root.GetLeftNode() != null ) {
          if ( root.GetLeftNode().GetData() != " " ) {  // left node is atom
            if ( first ) {
              System.out.print( sSpace + "  " ) ;
              root.GetLeftNode().Printf() ;
            } // if
            else {
              // System.out.print( " "  ) ;
              root.GetLeftNode().Printf() ;
              first = true ; 
            } // else
            
            sLP = true ;
            sExist = true ;
          } // if
        
          else if ( root.GetLeftNode().GetData() == " " ) { // left node is a exp
            first = true ;
            sLeftTree = true ;
            sSpace = sSpace + "  " ;
            PrettyPrint( root.GetLeftNode() ) ;
            sSpace = sSpace.substring( 2 ) ;
            if ( sSpace.equals( "" ) ) sLeftTree = false ;
          } // else if
        } // if
       
        if ( root.GetData() != " " && root.GetData() != "nil" ) {
          System.out.println( sSpace + "  ."  ) ;
          System.out.print( sSpace + "  " ); 
          root.Printf();
        } // if
        
        root = root.GetRightNode() ;
      } // while
      
      
      // System.out.println( sSpace +")" ) ;
      if ( sQuote ) System.out.println( sSpace + ")" ) ;
      else if ( !sLeftTree ) System.out.println( ")" ) ;
      else System.out.println( sSpace + ")" ) ;
      
    } // if 
    
    
    else if ( root != null && !sPrint ) { // atom
      System.out.print( sSpace ) ;
      root.Printf();
    } // else if
    
    else {
      
    } // else 
  } // PrettyPrint()

 
  public static char PeekChar( int tempsChar ) {

    if ( sLineInput.length() == tempsChar )
      return '\0';

    else {
      tempsChar++;
      return sLineInput.charAt( tempsChar - 1 );
    } // else 
  } // PeekChar()
  
  public static char PeekCharNoSpace( int tempsChar ) {
    if ( sLineInput.length() == tempsChar )
      return '\0';
    
    while ( sLineInput.charAt( tempsChar ) == ' ' ) {
      tempsChar++ ;
      if ( sLineInput.length() == tempsChar )
        return '\0' ;
    } // while
    
    return sLineInput.charAt( tempsChar ) ;
  } // PeekCharNoSpace()

  
  
  public static char PeekChar() throws Exception {
    
    if ( sca.hasNextLine() && sLineInput.length() == sChar ) { // read nextLine if the char is last one
      sChar = 0 ;
      sColumn = 0 ;
      sLineInput = sca.nextLine() ;
      sLine++ ;
      // if ( CheckLine( sLineInput ) ) sca.nextLine() ;
      return ' '  ;
    } // if
  
    else if ( !sca.hasNextLine() && sLineInput.length() == sChar ) { // end
      
      return '\0' ;
    } // else if
    
    else { // base case
      sChar++;
      sColumn++ ;
      return sLineInput.charAt( sChar - 1 ) ;
    } // else 
  } // PeekChar()

  public static Token GetToken() throws Exception {
    String stoken = "" ;
    sWhiteSpace = false ;
    
    boolean doubleQ = false ;
    boolean line = false ;
    
    char temp = GetChar() ;
    char peek = PeekChar( sChar ) ;
  
    
    while ( temp != '\0' ) {
  
      if ( temp == '\"' && !doubleQ ) {
        if ( sChar == sLineInput.length() ) {
          int errorcol = sColumn + 1 ;
          sErrorMsg = "ERROR (no closing quote) : END-OF-LINE encountered at Line " + sLine + 
                      " Column " + errorcol ;
          throw null ;
        } // if
        
        stoken = stoken + temp;
        doubleQ = true ;
      } // if
  
      else if ( doubleQ ) {
        
        if ( temp == '\"' ) { 
          
          if ( stoken.charAt( stoken.length() - 1 ) != '\\' ) {
            doubleQ = false;
            stoken = stoken + temp ;
            return new Token( stoken ) ;
          } // if
          
          else {
            stoken = stoken + temp ;
            if ( sChar == sLineInput.length() ) {
              int errorcol = sColumn + 1 ;
              sErrorMsg = "ERROR (no closing quote) : END-OF-LINE encountered at Line " + sLine + 
                          " Column " + errorcol ;
              throw null ;
            } // if
            
          } // else 
          
        } // if
  
        else if ( peek == '\0' ) { // line end
          int errorcol = sColumn + 1 ;
          sErrorMsg = "ERROR (no closing quote) : END-OF-LINE encountered at Line " + sLine + 
                      " Column " + errorcol ;
          throw null ;
        } // else if
        
        else
          stoken = stoken + temp;
      } // if
  
      else if ( temp == ' ' ) {
        if ( stoken != "" ) {
          sChar-- ;
          sColumn-- ;
          // sWhiteSpace = true ;
          return new Token( stoken ) ;
        } // if
      } // else if
  
      else if ( temp == ';' ) {
        sChar = sLineInput.length();
        if ( stoken != "" )
          return new Token( stoken );
      } // else if
  
      else if ( temp == '(' ) 
        return new Token( "(" ) ;
      else if ( temp == ')' )
        return new Token( ")" );
      else if ( temp == '\'' ) 
        return new Token( "'" ) ;
      else if ( temp == '.' && peek == ' ' ) {
        if ( stoken != "" ) {
          stoken = stoken + temp ;
          return new Token( stoken ) ;
        } // if
        
        return new Token( "." ) ;
      } // else if
        
 
  
      
      else {
        stoken = stoken + temp ;
        // System.out.println( stoken );
        
        
        if ( peek == '(' || peek == ')' || peek == '\'' ) {
          if ( stoken != "" ) {
            return new Token( stoken ) ;
          } // if
        } // if
        
        else if ( peek == '\0' ) {
          if ( stoken != "" ) return new Token( stoken ) ;
        } // else if
        
        else if ( peek == '\"' && !doubleQ ) {
          if ( stoken != "" ) {
            return new Token( stoken ) ;
          } // if
        } // else if
  
      } // else 
  
  
      temp = GetChar();
      peek = PeekChar( sChar ) ;
      
    } // while
    
    if ( temp == '\0' ) {
      if ( stoken != "" ) return new Token( stoken ) ;
      else {
        sErrorMsg = "ERROR (no more input) : END-OF-FILE encountered" ;
        throw null ;
      } // else 
    } // if
  
    return null ;
  
  } // GetToken() 
  

  
  public static char GetChar() throws Exception {
    return PeekChar() ;
  } // GetChar() 
  
  
} // class Main()

class Node { // Token Node
  private Token mdata  ; // set Token
  
  private Node mleftChild = null ; // left pointer
  private Node mrightChild = null ; // right pointer
  
  public Node( Token sdata ) { // construct
    this.mdata = sdata ;
  } // Node()
  
  public Node() { // construct 2 
    this.mdata = new Token( " " ) ;
    this.mleftChild = null ;
    this.mrightChild = null ;
  } // Node()
  
  public String GetData() { // function to get Token
    return this.mdata.Get() ;
  } // GetData()
  
  public String GetTokenType() { // function to get data Type
    return this.mdata.GetType() ;
  } // GetTokenType()
  
  public void SetLeft( Node sleftChild ) { // function to set left pointer
    this.mleftChild = sleftChild ;
  } // SetLeft()
  
  public void SetRight( Node srightChild ) { // function to set right pointer
    this.mrightChild = srightChild ;
  } // SetRight()
  
  public Node GetLeftNode() { // function to get left pointer
    return this.mleftChild ;
  } // GetLeftNode()
  
  public Node GetRightNode() { // function to get right pointer
    return this.mrightChild ;
  } // GetRightNode()
  
  public Token GetToken() {
    return this.mdata ;
  } // GetToken()
  
  public void Printf() { // function to print
    this.mdata.Printf() ;
  } // Printf()
  
  public void SetType( String type ) {
    this.mdata.SetType( type ) ;
  } // SetType()

} // class Node()

class Token { // basic Token Object
  private String moToken ; // Token can print
  private String mpToken ; // have to process Token
  private String mType ; // token type
  private boolean matom = false ;
  private boolean mquote = false ;
  
  public Token( String IToken ) { // construct
    this.mpToken = IToken ; // put in the object
    SetType() ; // define type 
  } // Token()
  
  
  public void SetType() {     
    if ( this.mpToken.equals( "(" ) ) this.mType = "LEFT-PAREN" ; // "(" object
    else if ( this.mpToken.equals( ")" ) ) this.mType = "RIGHT-PAREN" ; // ")" object
    else if ( this.mpToken.equals( "." ) ) this.mType = "DOT" ; // "." object
    else if ( this.mpToken.equals( "t" ) || this.mpToken.equals( "#t" ) ) { // "#t" object
      this.mType = "T" ;
      this.mpToken = "#t" ;
      this.matom = true ;
    } // else if
      
    else if ( this.mpToken.equals( "nil" ) || this.mpToken.equals( "#f" ) ) { // "nil" object
      this.mType = "NIL" ;
      this.mpToken = "nil" ;
      this.matom = true ;
    } // else if 
    
    else if ( this.mpToken.equals( "'" ) ) {
      this.mType = "cmd" ; // "'" object
      this.mpToken = "quote" ;
      this.mquote = true ;
      this.matom = true ;
    } // else if
    else if ( this.mpToken.equals( ";" ) ) this.mType = "semicolon" ; // ";" object
    
    else if ( CheckCommand() ) {
      this.mType = "cmd" ;
      this.matom = true ;
    } // else if
    
    else if ( CheckInt() ) { // check integer or not 
      this.mType = "INT" ;
      CountInt() ; // set number
      this.matom = true ;
    } // else if
    
    else if ( CheckFloat() ) { // check float or not
      this.mType = "FLOAT" ; 
      CountFloat() ; // set Float can print
      this.matom = true ;
    } // else if
    
    else if ( CheckString() ) {
      this.mpToken = CheckStringLine() ;
      this.mType = "STRING" ; // check string or not
      this.matom = true ;
    } // else if
    
    else {
      this.mType = "SYMBOL" ; // other is symbol
      this.matom = true ;
    } // else 
    
  } // SetType()
  

  
  public boolean CheckInt() { // check integer or not
    for ( int i = 0 ; i < this.mpToken.length() ; i++ ) { // run every character
      if ( i == 0 ) { // check first character
        if ( Character.isDigit( this.mpToken.charAt( i ) ) == false && this.mpToken.charAt( i ) != '+' &&
             this.mpToken.charAt( i ) != '-' ) return false ;
      } // if
      
      else if ( Character.isDigit( this.mpToken.charAt( i ) ) == false )
        return false ; // if one character is not digit 
      
    } // for
    
    return true ; // every character is like integer format
  } // CheckInt()
  
  public boolean CheckFloat() { // check float or not
    boolean dot = false ; // boolean "."
    boolean sign = false ; // boolean '+' or '-'
    for ( int i = 0 ; i < this.mpToken.length() ; i++ ) { // run every character
      if ( i == 0 ) { // check first one
        if ( Character.isDigit( this.mpToken.charAt( i ) ) == false && this.mpToken.charAt( i ) != '+' &&
             this.mpToken.charAt( i ) != '-' && this.mpToken.charAt( i ) != '.' ) 
          return false ;
        else if ( this.mpToken.charAt( i ) == '.' ) dot = true ; // example .5
        else if ( this.mpToken.charAt( i ) == '+' || this.mpToken.charAt( i ) == '-' ) 
          sign = true ; // +5
      } // if
      
      else if ( Character.isDigit( this.mpToken.charAt( i ) ) == false ) {
        if ( this.mpToken.charAt( i ) == '.' ) { // check '.'
          if ( dot ) return false ; // dot second time
          else {
            dot = true ;
            if ( i == this.mpToken.length() - 1 && this.mpToken.length() == 2 && sign ) 
              return false ; // +. -.
          } // else 
        } // if
        
        else return false ;
      } // else if
      
    } // for
    
    return true ;
  
  } // CheckFloat()
  
  public boolean CheckString() { // check String or not
    int doubleQ = 0 ; // boolean "
    if ( this.mpToken.charAt( 0 ) == '\"' && this.mpToken.charAt( this.mpToken.length() - 1 ) == '\"' ) {
      return true ;
    } // if
    
    else return false ;
  } // CheckString()
  
  public boolean CheckCommand() {
    if ( this.mpToken.equals( "exit" ) ) return true ;
    if ( this.mpToken.equals( "cons" ) ) return true ;
    if ( this.mpToken.equals( "list" ) ) return true ;
    
    if ( this.mpToken.equals( "quote" ) ) return true ;
    
    if ( this.mpToken.equals( "define" ) ) return true ;
    if ( this.mpToken.equals( "car" ) ) return true ;
    if ( this.mpToken.equals( "cdr" ) ) return true ;
    
    if ( this.mpToken.equals( "atom?" ) ) return true ;
    if ( this.mpToken.equals( "pair?" ) ) return true ;
    if ( this.mpToken.equals( "list?" ) ) return true ;
    if ( this.mpToken.equals( "null?" ) ) return true ;
    if ( this.mpToken.equals( "integer?" ) ) return true ;
    if ( this.mpToken.equals( "real?" ) ) return true ;
    if ( this.mpToken.equals( "number?" ) ) return true ;
    if ( this.mpToken.equals( "string?" ) ) return true ;
    if ( this.mpToken.equals( "boolean?" ) ) return true ;
    if ( this.mpToken.equals( "symbol?" ) ) return true ;
    
    if ( this.mpToken.equals( "+" ) ) return true ;
    if ( this.mpToken.equals( "-" ) ) return true ;
    if ( this.mpToken.equals( "*" ) ) return true ;
    if ( this.mpToken.equals( "/" ) ) return true ;
    
    if ( this.mpToken.equals( "not" ) ) return true ;
    if ( this.mpToken.equals( "and" ) ) return true ;
    if ( this.mpToken.equals( "or" ) ) return true ;
    
    if ( this.mpToken.equals( ">" ) ) return true ;
    if ( this.mpToken.equals( ">=" ) ) return true ;
    if ( this.mpToken.equals( "<" ) ) return true ;
    if ( this.mpToken.equals( "<=" ) ) return true ;
    if ( this.mpToken.equals( "=" ) ) return true ;
    if ( this.mpToken.equals( "string-append" ) ) return true ;
    if ( this.mpToken.equals( "string>?" ) ) return true ;
    if ( this.mpToken.equals( "string<?" ) ) return true ;
    if ( this.mpToken.equals( "string=?" ) ) return true ;
    
    if ( this.mpToken.equals( "eqv?" ) ) return true ;
    if ( this.mpToken.equals( "equal?" ) ) return true ;
    
    if ( this.mpToken.equals( "begin" ) ) return true ; 
    
    if ( this.mpToken.equals( "if" ) ) return true ;
    if ( this.mpToken.equals( "cond" ) ) return true ;
    
    if ( this.mpToken.equals( "clean-environment" ) ) return true ;
    
    if ( this.mpToken.equals( "let" ) ) return true ;
    if ( this.mpToken.equals( "lambda" ) ) return true ;
    
    
    return false ;
  } // CheckCommand()
  
  
  public String CheckStringLine() { // check String or not
    boolean doubleQ = false ; // boolean "
    boolean line = false ;
    String temp = "" ;
    String back = "" ;
    for ( int i = 0 ; i < this.mpToken.length() ; i++ ) {
     
      if ( line ) {
        if ( this.mpToken.charAt( i ) == '\\' ) 
          temp = temp + this.mpToken.charAt( i ) ;
        else if ( this.mpToken.charAt( i ) == 't' )
          temp = temp + '\t' ;
        else if ( this.mpToken.charAt( i ) == '\"' )
          temp = temp + '\"' ;
        else if ( this.mpToken.charAt( i ) == 'n' ) 
          temp = temp + '\n' ;

        else
          temp = temp + "\\" + this.mpToken.charAt( i ) ;

        line = false ;
      } // if
      
      else if ( this.mpToken.charAt( i ) == '\\' ) {
        line = true ;
      } // if
        
      else
        temp = temp + this.mpToken.charAt( i ) ;
       
    } // for

    return temp ;

  } // CheckStringLine() 
  
  
  public void CountInt() { // set integer
    if ( this.mpToken.charAt( 0 ) == '-' ||  Character.isDigit( this.mpToken.charAt( 0 ) ) )
      this.moToken = this.mpToken ;
    else { // +5
      this.moToken = this.mpToken.substring( 1 ) ; 
    } // else 
  } // CountInt()
  
  public void CountFloat() { // setFloat
    if ( this.mpToken.charAt( 0 ) == '+' ) 
      this.moToken = String.format( "%.3f", Float.parseFloat( this.mpToken.substring( 1 ) ) ) ;
    else if ( this.mpToken.charAt( 0 ) == '-' ) 
      this.moToken = String.format( "%.3f", Float.parseFloat( this.mpToken.substring( 1 ) ) * -1 ) ;
    else this.moToken = String.format( "%.3f", Float.parseFloat( this.mpToken ) ) ;
  } // CountFloat()
  
  
  public void Printf() { // function print
    if ( this.mType.equals( "INT" ) )
      System.out.println( this.moToken ) ;
    else if ( this.mType.equals( "FLOAT" ) )
      System.out.printf( "%.3f\n", Float.parseFloat( this.moToken ) ) ;
    else 
      System.out.println( this.mpToken );
     
  } // Printf()
  
  public String Get() { // function to outside call
    if ( this.mType.equals( "INT" ) || this.mType.equals( "FLOAT" ) ) return this.moToken ;
    else return this.mpToken ;
  } // Get()
  
  public String GetType() { // function to let outside getType
    return this.mType ;
  } // GetType()
  
  public boolean GetAtom() {
    return this.matom ;
  } // GetAtom()
  
  public boolean GetQuote() {
    return this.mquote ;
  } // GetQuote()
  
  public void SetType( String type ) {
    this.mType = type ;
  } // SetType()
  
} // class Token()



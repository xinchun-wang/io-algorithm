package com.vip.realtime.io.algorithem.tree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

/**
 * https://blog.csdn.net/zlp1992/article/details/51406067
 */
public class TreeNode {

  private int value;
  protected TreeNode left;
  protected TreeNode right;

  public TreeNode(int value){
    this.value = value;
  }

  public static void visit(TreeNode node) {
    System.out.print(node.value + " ");
  }

  public static void preOrder(TreeNode node){
    if(node == null){
      return;
    }
    visit(node);
    preOrder(node.left);
    preOrder(node.right);
  }

  public static void middleOrder(TreeNode node){
    if(node == null){
      return;
    }
    middleOrder(node.left);
    visit(node);
    middleOrder(node.right);
  }

  public static void postOrder(TreeNode node){
    if(node == null){
      return;
    }
    postOrder(node.left);
    postOrder(node.right);
    visit(node);
  }

  public static List<Integer> preOrderTraversal(TreeNode treeNode){
    List<Integer> resultList = new ArrayList<>();
    Stack<TreeNode> treeStack = new Stack<>();
    //如果为空树则返回
    if(treeNode == null) {
      return resultList;
    }

    treeStack.push(treeNode);

    while(!treeStack.isEmpty()){
      TreeNode node = treeStack.pop();
      if(node != null){
        //访问根节点
        resultList.add(node.value);
        //入栈右孩子
        treeStack.push(node.right);
        //入栈左孩子
        treeStack.push(node.left);
      }
    }
    return resultList;

  }

    @Override
  public String toString(){
    return "value : " + value;
  }

  private static TreeNode createNode(int value){
    TreeNode treeNode = new TreeNode(value);
    return  treeNode;
  }

  public static void main(String[] args){

    TreeNode treeNode = new TreeNode(6);
    treeNode.left = createNode(2);
    treeNode.right = createNode(8);

    treeNode.left.left = createNode(0);
    treeNode.left.left.right = createNode(1);

    treeNode.left.right = createNode(4);
    treeNode.left.right.right = createNode(5);
    treeNode.right.right = createNode(9);

    preOrder(treeNode);
    System.out.println();
    middleOrder(treeNode);
    System.out.println();
    postOrder(treeNode);
    System.out.println();


    List<Integer> treeOrder = preOrderTraversal(treeNode);
    System.out.println(treeOrder);

    Collections.sort(treeOrder);
    Collections.reverse(treeOrder);
  }


}

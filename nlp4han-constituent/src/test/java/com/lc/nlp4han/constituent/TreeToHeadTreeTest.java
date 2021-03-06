package com.lc.nlp4han.constituent;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.lc.nlp4han.constituent.AbstractHeadGenerator;
import com.lc.nlp4han.constituent.BracketExpUtil;
import com.lc.nlp4han.constituent.HeadGeneratorCollins;
import com.lc.nlp4han.constituent.HeadRuleSetCTB;
import com.lc.nlp4han.constituent.HeadTreeNode;
import com.lc.nlp4han.constituent.TreeNode;
import com.lc.nlp4han.constituent.TreeToHeadTree;
import com.lc.nlp4han.constituent.lex.HeadTreeNodeForCollins;
import com.lc.nlp4han.constituent.lex.TreeToHeadTreeForCollins;

public class TreeToHeadTreeTest
{
	@Test
	public void testGenerateHeadWords()
	{

		AbstractHeadGenerator headGen = new HeadGeneratorCollins(new HeadRuleSetCTB());
		TreeNode tree1 = BracketExpUtil.generateTreeNoTopBracket(
				"(ROOT(IP(NP(NT 一九九七年))(NP(NP(NP(NN 内地))(CC 与)(NP(NR 香港)))(NP(NN 经贸)(NN 交流)))(VP(VA 活跃))))");
		HeadTreeNode headTree1 = TreeToHeadTree.treeToHeadTree(tree1, headGen);
		String result1 = "(IP{活跃[VA]}(NP{一九九七年[NT]}(NT{一九九七年[NT]} 一九九七年[0]))(NP{交流[NN]}(NP{内地[NN]}(NP{内地[NN]}(NN{内地[NN]} 内地[1]))(CC{与[CC]} 与[2])(NP{香港[NR]}(NR{香港[NR]} 香港[3])))(NP{交流[NN]}(NN{经贸[NN]} 经贸[4])(NN{交流[NN]} 交流[5])))(VP{活跃[VA]}(VA{活跃[VA]} 活跃[6])))";
		assertEquals(headTree1.toString(), result1);

		TreeNode tree2 = BracketExpUtil.generateTreeNoTopBracket(
				"(ROOT(IP(LCP(NP(NP(NR 常州市))(PP(P 沿)(NP(NP(NN 运河))(QP(CD 两))(NP(NN 岸))))(QP(CD 零点五)(CLP(M 平方公里)))(NP(NN 范围)))(LC 内))(PU ，)(NP(ADJP(JJ 大型))(NP(NN 厂)(NN 矿)(NN 企业)))(VP(VE 有)(QP(CD 一百八十六)(CLP(M 家))))(PU 。)))");
		HeadTreeNode headTree2 = TreeToHeadTree.treeToHeadTree(tree2, headGen);
		String result2 = "(IP{有[VE]}(LCP{内[LC]}(NP{范围[NN]}(NP{常州市[NR]}(NR{常州市[NR]} 常州市[0]))(PP{沿[P]}(P{沿[P]} 沿[1])(NP{岸[NN]}(NP{运河[NN]}(NN{运河[NN]} 运河[2]))(QP{两[CD]}(CD{两[CD]} 两[3]))(NP{岸[NN]}(NN{岸[NN]} 岸[4]))))(QP{零点五[CD]}(CD{零点五[CD]} 零点五[5])(CLP{平方公里[M]}(M{平方公里[M]} 平方公里[6])))(NP{范围[NN]}(NN{范围[NN]} 范围[7])))(LC{内[LC]} 内[8]))(PU{，[PU]} ，[9])(NP{企业[NN]}(ADJP{大型[JJ]}(JJ{大型[JJ]} 大型[10]))(NP{企业[NN]}(NN{厂[NN]} 厂[11])(NN{矿[NN]} 矿[12])(NN{企业[NN]} 企业[13])))(VP{有[VE]}(VE{有[VE]} 有[14])(QP{一百八十六[CD]}(CD{一百八十六[CD]} 一百八十六[15])(CLP{家[M]}(M{家[M]} 家[16]))))(PU{。[PU]} 。[17]))";
		assertEquals(headTree2.toString(), result2);
		
		HeadTreeNodeForCollins headTree3=TreeToHeadTreeForCollins.treeToHeadTree(tree2, headGen);
		String CollinsResult="(IP{有[VE true 3]}(LCP{内[LC false 1]}(NP{范围[NN false 3]}(NPB{常州市[NR false 0]}(NR{常州市[NR false 0]} 常州市[0]))(PP{沿[P false 0]}(P{沿[P false 0]} 沿[1])(NP{岸[NN false 2]}(NPB{运河[NN false 0]}(NN{运河[NN false 0]} 运河[2]))(QP{两[CD false 0]}(CD{两[CD false 0]} 两[3]))(NPB{岸[NN false 0]}(NN{岸[NN false 0]} 岸[4]))))(QP{零点五[CD false 0]}(CD{零点五[CD false 0]} 零点五[5])(CLP{平方公里[M false 0]}(M{平方公里[M false 0]} 平方公里[6])))(NPB{范围[NN false 0]}(NN{范围[NN false 0]} 范围[7])))(LC{内[LC false 0]} 内[8]))(PU{，[PU false 0]} ，[9])(NP{企业[NN false 1]}(ADJP{大型[JJ false 0]}(JJ{大型[JJ false 0]} 大型[10]))(NPB{企业[NN false 2]}(NN{厂[NN false 0]} 厂[11])(NN{矿[NN false 0]} 矿[12])(NN{企业[NN false 0]} 企业[13])))(VP{有[VE true 0]}(VE{有[VE true 0]} 有[14])(QP{一百八十六[CD false 0]}(CD{一百八十六[CD false 0]} 一百八十六[15])(CLP{家[M false 0]}(M{家[M false 0]} 家[16]))))(PU{。[PU false 0]} 。[17]))";	
		assertEquals(headTree3.toString(), CollinsResult);
	}
}

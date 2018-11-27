package com.lc.nlp4han.constituent.unlex;

import java.util.ArrayList;

import com.lc.nlp4han.constituent.ConstituentMeasure;
import com.lc.nlp4han.constituent.ConstituentTree;
import com.lc.nlp4han.constituent.TreeNode;
import com.lc.nlp4han.constituent.pcfg.ConstituentParserCKYLoosePCNF;
import com.lc.nlp4han.constituent.pcfg.PCFG;
import com.lc.nlp4han.constituent.pcfg.TreeNodeUtil;
import com.lc.nlp4han.ml.util.Evaluator;

/**
 * @author 王宁
 * 
 */
public class EvaluatorLatentAnnotation extends Evaluator<ConstituentTree>
{
	/**
	 * 句法分析模型得到一颗句法树
	 */
	private ConstituentParserLatentAnnotation parser;

	/**
	 * 句法树中的短语分析评估
	 */
	private ConstituentMeasure measure;

	private long count = 0;
	private long totalTime = 0;

	public ConstituentMeasure getMeasure()
	{
		return measure;
	}

	public void setMeasure(ConstituentMeasure measure)
	{
		this.measure = measure;
	}

	public EvaluatorLatentAnnotation(ConstituentParserLatentAnnotation parser)
	{
		this.parser = parser;
	}

	public EvaluatorLatentAnnotation(Grammar gLatentAnnotation, PCFG p2nf, double pruneThreshold, boolean secondPrune,
			boolean prior)
	{
		this.parser = new ConstituentParserLatentAnnotation(
				new ConstituentParserCKYLoosePCNF(p2nf, pruneThreshold, secondPrune, prior), gLatentAnnotation);
	}

	@Override
	protected ConstituentTree processSample(ConstituentTree sample)
	{
		TreeNode rootNodeRef = sample.getRoot();
		ArrayList<String> words = new ArrayList<String>();
		ArrayList<String> poses = new ArrayList<String>();
		TreeNodeUtil.getWordsAndPOSFromTree(words, poses, rootNodeRef);
		String[] words1 = new String[words.size()];
		String[] poses1 = new String[poses.size()];
		for (int i = 0; i < words.size(); i++)
		{
			words1[i] = words.get(i);
			poses1[i] = poses.get(i);
		}

		long start = System.currentTimeMillis();

		ConstituentTree treePre = parser.parse(words1, poses1);
		long thisTime = System.currentTimeMillis() - start;
		totalTime += thisTime;
		count++;

		System.out.println(
				"句子长度：" + words.size() + " 平均解析时间：" + (totalTime / count) + "ms" + " 本句解析时间：" + thisTime + "ms");

		try
		{
			if (treePre == null)
			{
				System.out.println("无法解析的句子： " + rootNodeRef.toString());
				measure.countNodeDecodeTrees(null);
				measure.update(rootNodeRef, new TreeNode());
			}
			else
			{
				Binarization.recoverBinaryTree(treePre.getRoot());
				measure.update(rootNodeRef, TreeUtil.removeParentLabel(treePre.getRoot()));
			}
		}
		catch (CloneNotSupportedException e)
		{
			e.printStackTrace();
		}

		return treePre;
	}
}
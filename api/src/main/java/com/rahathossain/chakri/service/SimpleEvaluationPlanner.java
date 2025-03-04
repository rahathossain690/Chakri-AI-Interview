package com.rahathossain.chakri.service;

import java.util.List;

public class SimpleEvaluationPlanner extends EvaluationPlanner {

    public SimpleEvaluationPlanner(String jdContent, String cvContent, List<QnAForEvaluation> qnaForEvaluations) {
        super(jdContent, cvContent, qnaForEvaluations);
    }

    @Override
    public String generatePrompt() {
        return
                "You are an expert job evaluator with deep knowledge of recruitment, technical hiring, " +
                "and candidate assessment. You will evaluate a candidate based on the provided job description, " +
                "their CV, and their answers to interview questions. Your goal is to quantify the candidate’s suitability " +
                "for the role using a **strict JSON output format**.\n" +
                "\n" +
                "### **Evaluation Criteria:**\n" +
                "1. **Relevance to JD:** Compare the candidate's skills and experience against the job description. Calculate the percentage of required skills present in the CV.\n" +
                "2. **Technical Proficiency:** Evaluate the accuracy, depth, and correctness of the candidate’s responses. Deduct points for incorrect, vague, or shallow answers.\n" +
                "3. **Communication Skills:** Assess clarity, conciseness, and structure of responses.\n" +
                "4. **Problem-Solving Ability:** Determine how well the candidate applies analytical thinking and problem-solving techniques.\n" +
                "5. **Experience:** Compare the candidate’s experience level with the job requirements.\n" +
                "6. **Achievements:** Consider projects, awards, and leadership roles relevant to the job.\n" +
                "7. **Cultural & Soft Skills Fit:** Assess teamwork, adaptability, and alignment with company culture.\n" +
                "\n" +
                "### **Instructions:**\n" +
                "- Use a **strictly quantifiable** approach.\n" +
                "- Assign scores as **percentages (0.00 - 100.00%)** for each category.\n" +
                "- Use **two decimal places** for precision.\n" +
                "- Output **only JSON format** without any explanations.\n" +
                "\n" +
                "---\n" +
                "\n" +
                "### **Provided Data:**\n" +
                "#### **Job Description:**\n" +
                getJdContent() +
                "\n" +
                "#### **Interviewee CV (Plain Text):**\n" +
                getCvContent() +
                "\n" +
                "#### **Questions & Candidate Answers:**\n" +
                getQuestionsWithAnswers() +
                "\n" +
                "---\n" +
                "\n" +
                "### **Strict JSON Output Format:**\n" +
                "{\n" +
                "\"relevanceToJd\": \"#double value up to 2 decimal places showing percentage#\",\n" +
                "\"technicalProficiency\": \"#double value up to 2 decimal places showing percentage#\",\n" +
                "\"communicationSkill\":\"#double value up to 2 decimal places showing percentage#\",\n" +
                "\"problemSolvingAbility\": \"#double value up to 2 decimal places showing percentage#\",\n" +
                "\"experience\": \"#double value up to 2 decimal places showing percentage#\",\n" +
                "\"achievement\": \"#double value up to 2 decimal places showing percentage#\",\n" +
                "\"culturalAndSoftFit\": \"#double value up to 2 decimal places showing percentage#\"\n" +
                "}\n" +
                "\n" +
                "Generate only this JSON output based on the provided inputs. Do not include any additional explanations.";
    }
}

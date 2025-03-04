package com.rahathossain.chakri.service;

public class SimpleQuestionPlanner extends QuestionPlanner {

    public SimpleQuestionPlanner(String jdContent, String cvContent) {
        super(jdContent, cvContent);
    }

    @Override
    public String generatePrompt() {
        return
                "You are an advanced AI specializing in talent evaluation, recruitment, and interview preparation. Your task is" +
                " to **generate 30 highly relevant interview questions** based on the given **Job Description (JD)** and **Interviewee CV**.\n" +
                "\n" +
                "Job Description:\n" +
                getJdContent() + "\n" +
                "CV:\n" +
                getCvContent() + "\n" +
                "## **Question Structure**\n" +
                "Each question must fall into one of the following categories:\n" +
                "- **Mode:** `\"Technical\"` (assessing job skills) OR `\"Behavioral\"` (assessing personality, teamwork, soft skills)\n" +
                "- **Type:** `\"Paragraph\"` (open-ended) OR `\"Multiple Choice\"` (predefined options)\n" +
                "- **Difficulty:** `\"Creative\"` (requires deep thinking, real-world application) OR `\"Straight Forward\"` (directly tests knowledge)\n" +
                "\n" +
                "## **Instructions**\n" +
                "1. **Analyze the Job Description** `{job_description}` to identify key skills, technologies, and competencies required for the role.\n" +
                "2. **Review the Interviewee’s CV** `{interviewee_cv}` to tailor questions based on their experience, skills, and projects.\n" +
                "3. Have all types, all modes, all difficulties equally.\n" +
                "3. Ask those questions which a real interviewer would do, rather than silly descriptive questions ask questions so that answers are objectively judgable.\n" +
                "5. **Generate 30 diverse interview questions** covering the following evaluation criteria:\n" +
                "   - **Relevance to JD** (Does the candidate's background match job needs?)\n" +
                "   - **Technical Proficiency** (Does the candidate demonstrate skill depth?)\n" +
                "   - **Communication Skills** (How well does the candidate articulate?)\n" +
                "   - **Problem-Solving Ability** (Can the candidate think critically?)\n" +
                "   - **Experience** (Does experience align with expectations?)\n" +
                "   - **Achievements** (What significant contributions have they made?)\n" +
                "   - **Cultural & Soft Skills Fit** (Do they align with company values?)\n" +
                "\n" +
                "## **Output Format**\n" +
                "- **Return ONLY a JSON array of Question Object** without explanations.\n" +
                "- **Each question must be structured as follows:**\n" +
                "    ### **Output Type (JSON)**\n" +
                "    The result should be strictly a **structured JSON array**, with each question following this Type:\n" +
                "    ```\n" +
                "    {\n" +
                "      \"mode\": \"Behavioral\" or \"Technical\",\n" +
                "      \"type\": \"Multiple Choice\" or \"Paragraph\",\n" +
                "      \"difficulty\": \"Straight Forward\" or \"Creative\",\n" +
                "      \"question\": \"#QUESTION STRING#\",\n" +
                "      \"options\": #ARRAY OF STRING. EMPTY FOR ALL TYPES OF QUESTION EXCEPT MULTIPLE CHOICE#\n" +
                "    }\n" +
                "    ```\n" +
                "    some example of final result\n" +
                "    ```json\n" +
                "    [\n" +
                "        {\n" +
                "          \"mode\": \"Technical\",\n" +
                "          \"type\": \"Paragraph\",\n" +
                "          \"difficulty\": \"Creative\",\n" +
                "          \"question\": \"#QUESTION STRING#\",\n" +
                "          \"options\": []\n" +
                "        },\n" +
                "        {\n" +
                "          \"mode\": \"Behavioral\",\n" +
                "          \"type\": \"Multiple Choice\",\n" +
                "          \"difficulty\": \"Straight Forward\",\n" +
                "          \"question\": \"#QUESTION STRING#\",\n" +
                "          \"options\": [\n" +
                "            \"#OPTION STRING 1#\",\n" +
                "            \"#OPTION STRING 2#\",\n" +
                "            \"#OPTION STRING 3#\",\n" +
                "            \"#OPTION STRING 4#\"\n" +
                "          ]\n" +
                "        }\n" +
                "    ]\n";
    }

}

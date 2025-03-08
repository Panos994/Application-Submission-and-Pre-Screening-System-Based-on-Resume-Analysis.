# Application Submission and Pre-Screening System Based on Resume Analysis  

## Overview  
This system automates **resume analysis and candidate scoring** using **Spring Boot** and **Vue3.js**. It scans resumes via **Apache PDFBox**, extracts relevant keywords, and assigns a score based on keyword matching with job requirements. Also, propose the top 3 jobs to a candidate based on his resume using Open AI's API service (AI agent - LLM) for generate responses.

## Features  
- **Resume Parsing with Apache PDFBox**  
- **Keyword-Based Scoring System**  
  - Matches keywords such as **Java, Spring Boot, experience level, etc.**  
  - Calculates a **score** based on relevance to the job posting  
- **Spring Boot Backend:** REST API for processing resumes  
- **Vue3.js Frontend:** Interactive UI for job applications and score visualization  

## Tech Stack  
- **Backend:** Spring Boot, Apache PDFBox, Java 
- **Frontend:** Vue3.js  
- **Database:**  PostgreSQL  
- **API Communication:** RESTful APIs
- ****External Systems:** minIO, Open AI API

## Repository Structure  
- The **master branch** contains the full source code.  

## Setup Instructions  
### Prerequisites  
- **Backend:** Java 17+, Maven  
- **Frontend:** Node.js, npm  

### Backend Setup (Spring Boot)  
1. Clone the repository:  
   ```bash
   git clone https://github.com/Panos994/Application-Submission-and-Pre-Screening-System-Based-on-Resume-Analysis..git

# Job Scheduler System

A full-stack job scheduling application with a **Java** backend and **TypeScript** frontend, containerized with Docker Compose.

---

## Table of Contents

- [Overview](#overview)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Environment Variables](#environment-variables)
  - [Running with Docker Compose](#running-with-docker-compose)
  - [Running Locally](#running-locally)
- [Usage](#usage)
- [Contributing](#contributing)
- [License](#license)

---

## Overview

The Job Scheduler System allows users to create, manage, and monitor scheduled jobs. It provides a REST API backend for job orchestration and a modern frontend UI for interacting with the scheduler.

---

## Tech Stack

| Layer     | Technology        |
|-----------|-------------------|
| Backend   | Java              |
| Frontend  | TypeScript        |
| Container | Docker / Docker Compose |

---

## Project Structure

```
job-scheduler-system/
├── backend/          # Java backend (REST API, job scheduling logic)
├── frontend/         # TypeScript frontend (UI)
├── docker-compose.yml
├── env.example       # Example environment variable configuration
└── .gitignore
```

---

## Getting Started

### Prerequisites

Make sure you have the following installed:

- [Docker](https://www.docker.com/) & [Docker Compose](https://docs.docker.com/compose/)
- [Java 17+](https://adoptium.net/) (for local backend development)
- [Node.js 18+](https://nodejs.org/) & npm/yarn (for local frontend development)

### Environment Variables

Copy the example env file and fill in the required values:

```bash
cp env.example .env
```

Edit `.env` with your configuration before running the application.

### Running with Docker Compose

The easiest way to run the full stack:

```bash
docker-compose up --build
```

This will start both the backend and frontend services. To stop:

```bash
docker-compose down
```

### Running Locally

**Backend:**

```bash
cd backend
./mvnw spring-boot:run   # or however the backend is configured
```

**Frontend:**

```bash
cd frontend
npm install
npm run dev
```

---

## Usage

Once running, open your browser and navigate to the frontend (default: `http://localhost:3000`) to:

- View all scheduled jobs
- Create new jobs with custom schedules
- Monitor job status and execution history
- Edit or delete existing jobs

The backend API is accessible at `http://localhost:8080` (default).

---

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/your-feature`)
3. Commit your changes (`git commit -m 'Add your feature'`)
4. Push to the branch (`git push origin feature/your-feature`)
5. Open a Pull Request

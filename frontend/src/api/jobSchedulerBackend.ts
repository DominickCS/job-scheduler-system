const url = import.meta.env.VITE_API_URL;

export async function CreateJob(jobName: string, jobDescription: string, jobType: string, scheduleType: string, cronExpression: string, fixedDelay: number, isEnabled: boolean, nextExecution: string, jobParameters: {
  emailTo: string,
  emailFrom: string,
  emailSubject: string,
  emailBody: string,
  url: string
}) {
  const response = await fetch(`${url}/jobs/create`, {
    method: "POST",
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({
      jobName: jobName,
      jobDescription: jobDescription,
      jobType: jobType,
      scheduleType: scheduleType,
      cronExpression: cronExpression,
      fixedDelay: fixedDelay,
      isEnabled: isEnabled,
      nextExecution: nextExecution,
      jobParameters: JSON.stringify(jobParameters)
    })
  })
  return response.json();
}

export async function getJobs() {
  const response = await fetch(`${url}/jobs`, {
    method: "GET",
    headers: {
      'Content-Type': 'application/json'
    }
  })

  return response.json();
}

export async function getJobExecutions() {
  const response = await fetch(`${url}/executions`, {
    method: "GET",
    headers: {
      'Content-Type': 'application/json'
    }
  })

  return response.json();
}

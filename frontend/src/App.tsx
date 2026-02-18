import { useState } from "react"

export default function App() {
  const [showCronExpressionBox, setShowCronExpressionBox] = useState(false);
  const [showFixedDelayBox, setShowFixedDelayBox] = useState(false);
  const [showEmailNotificationModal, setShowEmailNotificationModal] = useState(false);
  const [showHealthCheckModal, setShowHealthCheckModal] = useState(false);

  function handleJobTypeSelection(e: React.FormEvent<HTMLInputElement>) {
    setShowEmailNotificationModal(e.currentTarget.id === 'emailNotification');
    setShowHealthCheckModal(e.currentTarget.id === 'healthCheck');
  }

  function handleScheduleSelection(e: React.FormEvent<HTMLInputElement>) {
    setShowCronExpressionBox(e.currentTarget.id === 'cron');
    setShowFixedDelayBox(e.currentTarget.id === 'delayed');
  }

  return (
    <>
      <div className="mx-auto px-8 max-w-md">
        <div id="emailNotificationParamsBox" className={showEmailNotificationModal ? "block" : "hidden"}>
          EMAIL BOX
        </div>
        <div id="healthCheckParamsBox" className={showHealthCheckModal ? "block" : "hidden"}>
          HEALTH CHECK BOX
        </div>
        <div>
          <form className="*:my-8">
            <div className="flex flex-col">
              <label htmlFor="jobName" className="font-extrabold">Job Name</label>
              <input name="jobName" id="jobName" type="text" className="border" />
              <label htmlFor="jobDescription" className="font-extrabold">Job Description</label>
              <input name="jobDescription" id="jobDescription" type="text" className="border" />
            </div>
            <label htmlFor="jobType" className="font-extrabold">Job Type</label>
            <div>
              <label htmlFor="emailNotification">Email Notification</label>
              <input name="jobType" id="emailNotification" type="radio" onChange={handleJobTypeSelection} />
              <label htmlFor="healthCheck">Health Check</label>
              <input name="jobType" id="healthCheck" type="radio" onChange={handleJobTypeSelection} />
            </div>
            <label htmlFor="scheduleType" className="font-extrabold">Schedule Type</label>
            <div>
              <label htmlFor="cron">CRON</label>
              <input name="scheduleType" id="cron" type="radio" onChange={handleScheduleSelection} />
              <label htmlFor="delayed">Fixed-delay</label>
              <input name="scheduleType" id="delayed" type="radio" onChange={handleScheduleSelection} />
              <label htmlFor="once">One-time</label>
              <input name="scheduleType" id="once" type="radio" onChange={handleScheduleSelection} />
            </div>
            <div id="cronExpInputBox" className={showCronExpressionBox ? "block" : "hidden"}>
              <label htmlFor="cronExpression">CRON Expression</label>
              <input type="text" name="cronExpression" id="cronExpression" />
            </div>
            <div id="fixedDelayInputBox" className={showFixedDelayBox ? "block" : "hidden"}>
              <label htmlFor="fixedDelay">Delay(ms)</label>
              <input type="number" name="fixedDelay" id="fixedDelay" />
            </div>
            <label htmlFor="jobSettings" className="font-extrabold">Job Settings</label>
            <div>
              <label htmlFor="isEnabled">Job Enabled?</label>
              <input type="checkbox" name="isEnabled" id="isEnabled" />
              <label htmlFor="jobExecuteTime">Execution Time</label>
              <input name="jobExecuteTime" id="jobExecuteTime" type="datetime-local" />
            </div>
            <input type="submit" value={"Create Job"} className="border p-2" />
          </form>
        </div>
      </div>
    </>
  )
}

call workflow_claim_update ('
{
  "claimlines": [
    {
      "batchId": 12,
      "claimLineId": 1,
      "claimLineNum": 1,
      "claimLineList": [
        {
          "status": "WAITING",
          "level": "ONE"
        },
        {
          "status": "OPEN",
          "level": "TWO"
        },
        {
          "status": null,
          "level": "THREE"
        },
        {
          "status": null,
          "level": "FOUR"
        }
      ],
      "claimStatusCode": null
    }
  ],
  "actionTakenBy": "abc2@aarete.com",
  "clientId": 1,
  "engagementId": 123,
  "claimExCodeLevel": "CLAIM_LINE",
  "exCodeBeanList": [
    {
      "exCodeType": "CLAIM_LINE",
      "exCode": "PTP",
      "confidence": 99,
      "isActive": false
    },
    {
      "exCodeType": "CLAIM_LINE",
      "exCode": "MUE",
      "confidence": 95,
      "isActive": true
    }
  ],
  "claimNum": 121313,
  "rejectReasonId": 0,
  "approverLevel": "AARETE_USER",
  "actionTaken": "ACCEPT"
}',0,'null');
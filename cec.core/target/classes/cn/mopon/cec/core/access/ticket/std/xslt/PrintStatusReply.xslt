<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="@* | node()">
		<xsl:copy>
			<xsl:apply-templates select="@* | node()" />
		</xsl:copy>
	</xsl:template>

	<xsl:template match="/OnlineTicketingServiceReply/QueryPrintReply/Order">
		<Order>
			<cinemaOrderCode><xsl:value-of select="@OrderCode" /></cinemaOrderCode>
			<voucher>
				<printCode><xsl:value-of select="@PrintNo" /></printCode>
				<verifyCode><xsl:value-of select="@VerifyCode" /></verifyCode>
				<status>
					<xsl:choose>
						<xsl:when test="Status='YES'">1</xsl:when>
						<xsl:when test="Status='No'">0</xsl:when>
					</xsl:choose>
				</status>
				<xsl:if test="string-length(PrintTime)>8">
				    <confirmPrintTime><xsl:value-of select="PrintTime"/></confirmPrintTime>
				</xsl:if>
			</voucher>
		</Order>
	</xsl:template>
</xsl:stylesheet>
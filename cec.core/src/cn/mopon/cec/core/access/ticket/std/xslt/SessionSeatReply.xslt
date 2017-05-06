<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="@* | node()">
		<xsl:copy>
			<xsl:apply-templates select="@* | node()" />
		</xsl:copy>
	</xsl:template>

	<xsl:template match="/OnlineTicketingServiceReply/QuerySessionSeatReply/SessionSeat">
		<xsl:variable name="sessionCode" select="@SessionCode"></xsl:variable>
		<showSeats>
			<xsl:for-each select="Seat">
				<ShowSeat>
					<code><xsl:value-of select="Code" /></code>
					<rowNum><xsl:value-of select="RowNum" /></rowNum>
					<colNum><xsl:value-of select="ColumnNum" /></colNum>
					<status>
						<xsl:choose>
							<xsl:when test="Status='Available'">1</xsl:when>
							<xsl:otherwise>0</xsl:otherwise>
						</xsl:choose>
					</status>
				</ShowSeat>
			</xsl:for-each>
		</showSeats>
	</xsl:template>
</xsl:stylesheet>
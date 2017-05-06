<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="@* | node()">
		<xsl:copy>
			<xsl:apply-templates select="@* | node()" />
		</xsl:copy>
	</xsl:template>

	<xsl:template match="/GetPlanSiteStateResult">
		<GetPlanSiteStateResult>
		    <ResultCode><xsl:value-of select="ResultCode"/></ResultCode>
			<showSeats>
				<xsl:for-each select="PlanSiteStates/PlanSiteState">
					<ShowSeat>
						<code><xsl:value-of select="SeatNo" /></code>
						<rowNum><xsl:value-of select="SeatRow" /></rowNum>
						<colNum><xsl:value-of select="SeatCol" /></colNum>
						<status>
							<xsl:choose>
								<xsl:when test="SeatState='0'">1</xsl:when>
								<xsl:otherwise>0</xsl:otherwise>
							</xsl:choose>
						</status>
					</ShowSeat>
				</xsl:for-each>
			</showSeats>
		</GetPlanSiteStateResult>
	</xsl:template>
</xsl:stylesheet>